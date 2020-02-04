package org.abigballofmud.azkaban.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;

import com.google.gson.Gson;
import org.abigballofmud.azkaban.common.domain.SpecifiedParamsResponse;
import org.abigballofmud.azkaban.common.params.PredefinedParams;
import org.abigballofmud.azkaban.common.params.SimpleTimeUnitEnum;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * description
 * </p>
 *
 * @author abigballofmud 2020/01/10 15:54
 * @since 1.0
 */
public class ParamsUtil {

    private ParamsUtil() {
        throw new IllegalStateException("context class");
    }

    private static RestTemplate restTemplate = RestTemplateUtil.getRestTemplate();
    private static Gson gson = new Gson();

    /**
     * 项目客制化需求，内置参数值从表里取
     */
    public static SpecifiedParamsResponse getSpecifiedParams(String url, Long tenantId, String jobName) {
        ResponseEntity<SpecifiedParamsResponse> responseEntity = restTemplate.getForEntity(
                String.format("%s/v2/%d/timestamp-controls/get-increment-param?timestampType=%s",
                        url,
                        tenantId,
                        jobName),
                SpecifiedParamsResponse.class);
        if (Objects.nonNull(Objects.requireNonNull(responseEntity.getBody()).getFailed())) {
            throw new IllegalStateException(String.format("请求查询内置参数值报错，%s", responseEntity.getBody()));
        }
        SpecifiedParamsContext.setSpecifiedParamsResponse(responseEntity.getBody());
        return responseEntity.getBody();
    }

    /**
     * 项目客制化需求，azkaban执行完毕后更新表里的内置参数值
     */
    public static void updateSpecifiedParams(Logger log, String url, Long tenantId, String jobName) {
        try {
            SpecifiedParamsResponse specifiedParams = SpecifiedParamsContext.current();
            log.info(String.format("context specifiedParams: %s", specifiedParams));
            HashMap<String, Object> body = new HashMap<>(8);
            body.put("tenantId", tenantId);
            body.put("timestampType", jobName);
            body.put("currentDateTime", specifiedParams.getCurrentDataTime());
            body.put("currentMaxId", specifiedParams.getCurrentMaxId());
            HttpEntity<String> requestEntity = new HttpEntity<>(gson.toJson(body), RestTemplateUtil.httpHeaders());
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    String.format("%s/v2/%d/timestamp-controls/update-increment", url, tenantId),
                    requestEntity, String.class);
            if (Objects.requireNonNull(requestEntity.getBody()).contains(PredefinedParams.FAILED)) {
                throw new IllegalStateException(String.format("azkaban执行完毕更新内置参数值报错，%s", responseEntity.getBody()));
            }
            log.info("内置参数值更新成功，" + requestEntity.getBody());
        } finally {
            SpecifiedParamsContext.clear();
        }
    }

    /**
     * 对内置参数的处理
     *
     * @param str                     替换的文本
     * @param specifiedParamsResponse 额外的内置参数值，比如自己给内置参数赋值
     * @return java.lang.String
     * @author abigballofmud 2020/2/4 11:25
     */
    public static String handlePredefinedParams(String str, SpecifiedParamsResponse specifiedParamsResponse) {
        Matcher matcher = PredefinedParams.PREDEFINED_PARAM_REGEX.matcher(str);
        while (matcher.find()) {
            // _p_current_data_time
            if (matcher.group(1).trim().contains(PredefinedParams.CURRENT_DATE_TIME)) {
                str = handleDateTime(str, matcher.group(1).trim(), specifiedParamsResponse);
            }
            // _p_last_date_time
            if (matcher.group(1).trim().contains(PredefinedParams.LAST_DATE_TIME)) {
                str = str.replaceAll(String.format("\\$\\{%s\\}", PredefinedParams.LAST_DATE_TIME),
                        specifiedParamsResponse.getLastDateTime());
            }
            // _p_last_date_time
            if (matcher.group(1).trim().contains(PredefinedParams.CURRENT_MAX_ID)) {
                str = str.replaceAll(String.format("\\$\\{%s\\}", PredefinedParams.CURRENT_MAX_ID),
                        specifiedParamsResponse.getCurrentMaxId());
            }
            // _p_last_max_id
            if (matcher.group(1).trim().contains(PredefinedParams.LAST_MAX_ID)) {
                str = str.replaceAll(String.format("\\$\\{%s\\}", PredefinedParams.LAST_MAX_ID),
                        specifiedParamsResponse.getLastMaxId());
            }
        }
        return str;
    }

    private static String handleDateTime(String str, String matcher, SpecifiedParamsResponse specifiedParamsResponse) {
        String[] splitArr = matcher.split(PredefinedParams.SPLIT_KEY);
        String originDataTime = Optional.ofNullable(specifiedParamsResponse.getCurrentDataTime()).orElse(splitArr[0]);
        int defaultSize = 2;
        if (splitArr.length > defaultSize) {
            //  _p_current_date_time:N:day
            String currentDateTime = genLocalDateTime(originDataTime, Long.valueOf(splitArr[1]), splitArr[2]);
            str = str.replaceAll(String.format("\\$\\{%s\\:%s\\:%s\\}", PredefinedParams.CURRENT_DATE_TIME, splitArr[1], splitArr[2]),
                    currentDateTime);
        } else if (splitArr.length == defaultSize) {
            // _p_current_date_time:yyyy-MM-dd HH:mm:ss
            String currentDateTime = LocalDateTime.parse(originDataTime).format(DateTimeFormatter.ofPattern(splitArr[1]));
            str = str.replaceAll(String.format("\\$\\{%s\\:%s\\}", splitArr[0], splitArr[1]), currentDateTime);
        } else {
            // _p_current_date_time
            String currentDateTime = LocalDateTime.parse(originDataTime).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            str = str.replaceAll(String.format("\\$\\{%s\\}", PredefinedParams.CURRENT_DATE_TIME),
                    currentDateTime);
        }
        return str;
    }

    private static String genLocalDateTime(String originDataTime, Long interval, String unit) {
        LocalDateTime localDateTime = LocalDateTime.parse(originDataTime);
        String currentDateTime;
        switch (SimpleTimeUnitEnum.valueOf(unit.toUpperCase())) {
            case YEAR:
                currentDateTime = localDateTime.minusYears(interval).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                break;
            case MONTH:
                currentDateTime = localDateTime.minusMonths(interval).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                break;
            case WEEK:
                currentDateTime = localDateTime.minusWeeks(interval).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                break;
            case DAY:
                currentDateTime = localDateTime.minusDays(interval).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                break;
            case HOUR:
                currentDateTime = localDateTime.minusHours(interval).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                break;
            case MIN:
                currentDateTime = localDateTime.minusMinutes(interval).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                break;
            case SEC:
                currentDateTime = localDateTime.minusSeconds(interval).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                break;
            default:
                throw new IllegalArgumentException(String.format("非法单位[%s]，单位[year，month，week，day，hour，min，sec]", unit));
        }
        return currentDateTime;
    }

}
