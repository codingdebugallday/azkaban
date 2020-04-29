package org.abigballofmud.azkaban.common.utils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.abigballofmud.azkaban.common.domain.EurekaAppInstance;
import org.abigballofmud.azkaban.common.exception.CustomerRuntimeException;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * eureka工具类
 * </p>
 *
 * @author isacc 2020/4/28 17:25
 * @since 1.0
 */
public class EurekaUtil {

    private final RestTemplate resttemplate;
    private final String eurekaUrl;

    public EurekaUtil(String eurekaUrl) {
        this.eurekaUrl = eurekaUrl;
        resttemplate = RestTemplateUtil.getRestTemplate();
    }

    public static EurekaUtil createEurekaUtilFromProperties(String hdspPropertiesPath) {
        String eurekaUrl = PropertiesUtil.getProperties(hdspPropertiesPath, "eureka.url");
        return new EurekaUtil(eurekaUrl);
    }

    public EurekaAppInstance getAllAppById(String appId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<EurekaAppInstance> responseEntity = resttemplate.exchange(
                eurekaUrl + "/eureka/apps/" + appId,
                HttpMethod.GET,
                entity,
                EurekaAppInstance.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        }
        throw new CustomerRuntimeException("get app instance from eureka error, response: " + responseEntity);
    }

    public EurekaAppInstance.ApplicationBean.InstanceBean getRandomAppById(String appId) {
        EurekaAppInstance eurekaAppInstance = getAllAppById(appId);
        List<EurekaAppInstance.ApplicationBean.InstanceBean> instanceList =
                eurekaAppInstance.getApplication().getInstance();
        return instanceList.get(ThreadLocalRandom.current().nextInt(instanceList.size()));
    }
}
