package org.abigballpfmud.azkaban.plugin.rest.service.impl;

import java.util.Objects;

import azkaban.utils.Props;
import org.abigballofmud.azkaban.common.exception.CustomerJobProcessException;
import org.abigballofmud.azkaban.common.exception.CustomerRuntimeException;
import org.abigballofmud.azkaban.common.utils.RestTemplateUtil;
import org.abigballpfmud.azkaban.plugin.rest.constants.Auth;
import org.abigballpfmud.azkaban.plugin.rest.constants.Key;
import org.abigballpfmud.azkaban.plugin.rest.exec.Exec;
import org.abigballpfmud.azkaban.plugin.rest.exec.HttpExec;
import org.abigballpfmud.azkaban.plugin.rest.model.Data;
import org.abigballpfmud.azkaban.plugin.rest.model.HttpResp;
import org.abigballpfmud.azkaban.plugin.rest.model.Payload;
import org.abigballpfmud.azkaban.plugin.rest.service.ExecuteJobService;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
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

    private Auth auth;
    private Exec exec;

    @Override
    public void executeJob(Props jobProps, Logger logger) throws CustomerJobProcessException {
        logger.info("start rest api job");
        check(jobProps);
        // 创建RestTemplate
        RestTemplate restTemplate = RestTemplateUtil.getRestTemplate();
        // http执行
        this.exec = new HttpExec(restTemplate, logger);
        // 设置认证Provider
        this.auth.authProvider().provide(restTemplate, jobProps, logger);
        // 封装参数
        Payload payload = genPayload(jobProps);
        // 获取结果
        Data<?> data = get(payload);
        logger.info("response data: " + data);
    }

    private Payload genPayload(Props jobProps) {
        return Payload.of()
                // 接口信息
                .putArgs(Key.EXTERNAL, jobProps.getString(Key.EXTERNAL, "true"))
                // 表达式、请求方式
                .putArgs(Key.METHOD, jobProps.getString(Key.METHOD, RequestMethod.GET.name()))
                .putArgs(Key.URI, jobProps.get(Key.URI))
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
                .putArgs(Key.QUERY, jobProps.getString(Key.QUERY, null))
                .putArgs(Key.BODY, jobProps.getString(Key.BODY, null))
                // api重试
                .putArgs(Key.ENABLED_RETRY, jobProps.getString(Key.BODY, "false"))
                .putArgs(Key.RETRY_NUMBER, jobProps.getString(Key.RETRY_NUMBER, "3"))
                .putArgs(Key.RETRY_INTERVAL, jobProps.getString(Key.RETRY_INTERVAL, "1"))
                .putArgs(Key.ENABLED_RETRY_EXPONENTIAL, jobProps.getString(Key.ENABLED_RETRY_EXPONENTIAL, "true"))
                // callback
                .putArgs(Key.ENABLED_CALLBACK, jobProps.getString(Key.ENABLED_CALLBACK, "false"))
                .putArgs(Key.CALLBACK_URI, jobProps.getString(Key.CALLBACK_URI, null))
                .putArgs(Key.CALLBACK_INTERVAL, jobProps.getString(Key.CALLBACK_INTERVAL, "5"))
                .putArgs(Key.CALLBACK_NUMBER, jobProps.getString(Key.CALLBACK_NUMBER, "3"))
                .putArgs(Key.ENABLED_CALLBACK_EXPONENTIAL, jobProps.getString(Key.ENABLED_CALLBACK_EXPONENTIAL, "true"))
                .putArgs(Key.CALLBACK_FINISH_SUCCESS, jobProps.getString(Key.CALLBACK_FINISH_SUCCESS, "true"));
    }

    private Data<?> get(Payload payload) throws CustomerJobProcessException {
        try {
            MutablePair<ResponseEntity<String>, ResponseEntity<String>> mutablePair = exec.doExec(payload);
            // api
            ResponseEntity<String> respEntity = mutablePair.getLeft();
            HttpResp resp = new HttpResp(respEntity.getHeaders(),
                    respEntity.getBody(),
                    respEntity.getStatusCode().getReasonPhrase(),
                    respEntity.getStatusCodeValue());
            if (!respEntity.getStatusCode().is2xxSuccessful()) {
                throw new CustomerJobProcessException(
                        String.format("request failed, response: %s", resp));
            }
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
            HttpResp resp = new HttpResp(e.getResponseHeaders(),
                    e.getResponseBodyAsString(),
                    e.getStatusText(),
                    e.getRawStatusCode());
            return new Data<>(resp);
        } catch (Exception e) {
            throw new CustomerRuntimeException("http exec failed", e);
        }
    }

    private void check(Props jobProps) {
        // 验证auth
        try {
            this.auth = Auth.valueOf(jobProps.getString(Key.AUTH, Auth.NONE.name()));
        } catch (Exception e) {
            throw new CustomerRuntimeException("Properties [" + Key.METHOD + "] invalid");
        }
    }


}
