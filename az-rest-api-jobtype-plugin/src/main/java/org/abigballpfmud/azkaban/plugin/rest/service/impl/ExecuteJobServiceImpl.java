package org.abigballpfmud.azkaban.plugin.rest.service.impl;

import java.util.Objects;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.common.constants.Auth;
import org.abigballofmud.azkaban.common.constants.JobPropsKey;
import org.abigballofmud.azkaban.common.constants.Key;
import org.abigballofmud.azkaban.common.exception.CustomerJobProcessException;
import org.abigballofmud.azkaban.common.exception.CustomerRuntimeException;
import org.abigballofmud.azkaban.common.utils.*;
import org.abigballpfmud.azkaban.plugin.rest.constants.CommonConstants;
import org.abigballpfmud.azkaban.plugin.rest.exec.Exec;
import org.abigballpfmud.azkaban.plugin.rest.exec.HttpExec;
import org.abigballpfmud.azkaban.plugin.rest.model.Data;
import org.abigballpfmud.azkaban.plugin.rest.model.HttpResp;
import org.abigballpfmud.azkaban.plugin.rest.model.Payload;
import org.abigballpfmud.azkaban.plugin.rest.service.ExecuteJobService;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * ExecuteJobServiceImpl
 * </p>
 *
 * @author isacc 2020/4/23 16:02
 * @since 1.0
 */
public class ExecuteJobServiceImpl implements ExecuteJobService {

    private Exec exec;
    private Logger log;

    @Override
    public void executeJob(Props jobProps, Logger logger) throws CustomerJobProcessException {
        logger.info("start rest api job");
        log = logger;
        // 封装参数 内部请求时需生成具体url
        if (!jobProps.getBoolean(Key.EXTERNAL, true)) {
            genHdspUrl(jobProps);
        }
        // 创建RestTemplate 并设置认证Provider
        RestTemplate restTemplate = RestTemplateUtil.getRestTemplateWithAuth(jobProps, logger);
        // http执行
        this.exec = new HttpExec(restTemplate, logger);
        // 涉及到内置参数替换，如url/query/body等
        Payload payload = genPayload(jobProps);
        // 获取结果
        Data<?> data = get(payload);
        logger.info("response data: " + data);
    }

    private void genHdspUrl(Props jobProps) {
        log.info("handle hdsp rest api, generate real url...");
        // 覆盖url相关的值
        String workDir = jobProps.getString(JobPropsKey.WORKING_DIR.getKey());
        String hdspPropertiesPath = CommonUtil.getHdspPropertiesPath(workDir);
        EurekaUtil eurekaUtil = EurekaUtil.createEurekaUtilFromProperties(hdspPropertiesPath);
        String gatewayAppName = PropertiesUtil.getProperties(hdspPropertiesPath, "app.gateway");
        // http://172.23.16.65:8080/
        String gatewayUrl = eurekaUtil.getRandomAppById(gatewayAppName).getHomePageUrl();
        // 覆盖rest.uri
        // 是否走网关
        String url = jobProps.get(Key.URI);
        if (jobProps.getBoolean(Key.USE_GATEWAY, true)) {
            log.info("real api url: " + gatewayUrl + url);
            jobProps.put(Key.URI, gatewayUrl + url);
        } else {
            String appUrl = eurekaUtil.getRandomAppById(jobProps.getString(Key.APP)).getHomePageUrl();
            log.info("real api url: " + appUrl + url);
            jobProps.put(Key.URI, appUrl + url);
        }
        // 覆盖 rest.callback.uri
        String callbackUri = jobProps.getString(Key.CALLBACK_URI, null);
        if (!StringUtils.isEmpty(callbackUri) && !callbackUri.startsWith(CommonConstants.HTTP)) {
            // 是否走网关
            if (jobProps.getBoolean(Key.CALLBACK_USE_GATEWAY, true)) {
                log.info("real callback url: " + gatewayUrl + callbackUri);
                jobProps.put(Key.CALLBACK_URI, gatewayUrl + callbackUri);
            } else {
                String appUrl = eurekaUtil.getRandomAppById(jobProps.getString(Key.CALLBACK_APP)).getHomePageUrl();
                log.info("real callback url: " + appUrl + callbackUri);
                jobProps.put(Key.CALLBACK_URI, appUrl + callbackUri);
            }
        }
    }

    private Payload genPayload(Props jobProps) {
        String jobName = jobProps.get(JobPropsKey.JOB_ID.getKey());
        ParamsUtil paramsUtil = new ParamsUtil(jobProps, log);
        // 内置参数 URI/QUERY/BODY/CALLBACK_URI
        return Payload.of()
                // 接口信息
                .putArgs(Key.EXTERNAL, jobProps.getString(Key.EXTERNAL, "true"))
                // 表达式、请求方式
                .putArgs(Key.METHOD, jobProps.getString(Key.METHOD, RequestMethod.GET.name()))
                .putArgs(Key.URI, paramsUtil.handlePredefinedParams(jobProps.get(Key.URI), jobName))
                .putArgs(Key.CONTENT_TYPE, jobProps.getString(Key.CONTENT_TYPE, "application/json"))
                // 授权信息
                .putArgs(Key.AUTH, jobProps.getString(Key.AUTH, Auth.NONE.name()))
                .putArgs(Key.GRANT_TYPE, jobProps.getString(Key.GRANT_TYPE, null))
                .putArgs(Key.CLIENT_ID, jobProps.getString(Key.CLIENT_ID, null))
                .putArgs(Key.CLIENT_SECRET, jobProps.getString(Key.CLIENT_SECRET, null))
                .putArgs(Key.TOKEN_URI, jobProps.getString(Key.TOKEN_URI, null))
                .putArgs(Key.USERNAME, jobProps.getString(Key.USERNAME, null))
                .putArgs(Key.PASSWORD, jobProps.getString(Key.PASSWORD, null))
                // 放入当前请求对象
                // .putArgs("request", jobProps.getString(Key.REQUEST))
                // 请求参数
                .putArgs(Key.HEADER, jobProps.getString(Key.HEADER, null))
                .putArgs(Key.QUERY, paramsUtil.handlePredefinedParams(jobProps.getString(Key.QUERY, null), jobName))
                .putArgs(Key.BODY, paramsUtil.handlePredefinedParams(jobProps.getString(Key.BODY, null), jobName))
                // api重试
                .putArgs(Key.ENABLED_RETRY, jobProps.getString(Key.BODY, "false"))
                .putArgs(Key.RETRY_NUMBER, jobProps.getString(Key.RETRY_NUMBER, "3"))
                .putArgs(Key.RETRY_INTERVAL, jobProps.getString(Key.RETRY_INTERVAL, "1"))
                .putArgs(Key.ENABLED_RETRY_EXPONENTIAL, jobProps.getString(Key.ENABLED_RETRY_EXPONENTIAL, "true"))
                // callback
                .putArgs(Key.ENABLED_CALLBACK, jobProps.getString(Key.ENABLED_CALLBACK, "false"))
                .putArgs(Key.CALLBACK_URI, paramsUtil.handlePredefinedParams(jobProps.getString(Key.CALLBACK_URI, null), jobName))
                .putArgs(Key.CALLBACK_INTERVAL, jobProps.getString(Key.CALLBACK_INTERVAL, "5"))
                .putArgs(Key.CALLBACK_NUMBER, jobProps.getString(Key.CALLBACK_NUMBER, "3"))
                .putArgs(Key.ENABLED_CALLBACK_EXPONENTIAL, jobProps.getString(Key.ENABLED_CALLBACK_EXPONENTIAL, "true"))
                .putArgs(Key.CALLBACK_FINISH_SUCCESS, jobProps.getString(Key.CALLBACK_FINISH_SUCCESS, "true"))
                .putArgs(Key.CALLBACK_RESPONSE_KEY, jobProps.getString(Key.CALLBACK_RESPONSE_KEY, null))
                .putArgs(Key.CALLBACK_RESPONSE_VALUE, jobProps.getString(Key.CALLBACK_RESPONSE_VALUE, null));
    }

    private Data<?> get(Payload payload) throws CustomerJobProcessException {
        try {
            MutablePair<ResponseEntity<String>, ResponseEntity<String>> mutablePair = exec.doExec(payload);
            // api
            ResponseEntity<String> respEntity = mutablePair.getLeft();
            HttpStatus statusCode = respEntity.getStatusCode();
            HttpResp resp = new HttpResp(respEntity.getHeaders(),
                    respEntity.getBody(),
                    statusCode.getReasonPhrase(),
                    respEntity.getStatusCodeValue());
            log.info("request success, code: " + statusCode.value());
            // callback
            if (Objects.isNull(mutablePair.getRight())) {
                if (Boolean.parseBoolean(payload.getOrThrow(Key.CALLBACK_FINISH_SUCCESS))) {
                    return new Data<>(MutablePair.of(resp, "the rest api is running or failed"));
                }
                throw new CustomerJobProcessException(String.format("api response: %s , callback: " +
                        "the rest api is still running or failed", resp));
            }
            return new Data<>(MutablePair.of(resp, mutablePair.getRight()));
        } catch (IllegalArgumentException | CustomerRuntimeException | CustomerJobProcessException e) {
            throw e;
        } catch (RestClientResponseException e) {
            throw new CustomerRuntimeException("RestClientResponseException", e);
        } catch (Exception e) {
            throw new CustomerRuntimeException("http exec failed", e);
        }
    }

}
