package org.abigballpfmud.azkaban.plugin.rest.service.impl;

import azkaban.utils.Props;
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
    public void executeJob(Props jobProps, Logger logger) {
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
                // 表达式、请求方式
                .putArgs(Key.METHOD, jobProps.getString(Key.METHOD, RequestMethod.GET.name()))
                .putArgs(Key.URI, jobProps.get(Key.URI))
                // 放入当前请求对象
                // .putArgs("request", jobProps.getString(Key.REQUEST))
                // 请求参数
                .putArgs(Key.HEADER, jobProps.getString(Key.HEADER, null))
                .putArgs(Key.QUERY, jobProps.getString(Key.QUERY, null))
                .putArgs(Key.BODY, jobProps.getString(Key.BODY, null));
    }

    private Data<?> get(Payload payload) {
        try {
            ResponseEntity<String> respEntity = exec.doExec(payload);
            HttpResp resp = new HttpResp(respEntity.getHeaders(),
                    respEntity.getBody(),
                    respEntity.getStatusCode().getReasonPhrase(),
                    respEntity.getStatusCodeValue());
            return new Data<>(resp);
        } catch (IllegalArgumentException | CustomerRuntimeException e) {
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
