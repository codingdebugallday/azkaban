package org.abigballofmud.azkaban.common.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
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

    private static final RestTemplate RESTTEMPLATE = RestTemplateUtil.getRestTemplate();
    private static final Gson GSON = new Gson();
    private static final String PARAM_PATTERN = "\\$\\{%s\\}";

    public static String getHdspCoreUrl(Logger log, String hdspPropertiesPath) {
        log.info("load hdsp.properties: " + hdspPropertiesPath);
        Properties properties = new Properties();
        try (InputStream in = new BufferedInputStream(new FileInputStream(hdspPropertiesPath))) {
            properties.load(in);
            return properties.getProperty("hdsp.core.url");
        } catch (IOException e) {
            throw new IllegalStateException("获取hdsp_core服务的url地址出错, " + e);
        }
    }

    /**
     * 项目客制化需求，内置参数值从表里取
     */
    public static SpecifiedParamsResponse getSpecifiedParams(Logger log, Long tenantId, String workDir, String jobName) {
        String hdspPropertiesPath = CommonUtil.getAzHomeByWorkDir(workDir) + "/conf/hdsp.properties";
        log.info("jobName: " + jobName);
        ResponseEntity<SpecifiedParamsResponse> responseEntity = RESTTEMPLATE.getForEntity(
                String.format("%s/v2/%d/timestamp-controls/get-increment-param?timestampType=%s",
                        ParamsUtil.getHdspCoreUrl(log, hdspPropertiesPath),
                        tenantId,
                        jobName),
                SpecifiedParamsResponse.class);
        if (Objects.nonNull(Objects.requireNonNull(responseEntity.getBody()).getFailed())) {
            // 不抛异常 继续执行
            log.warn(String.format("请求查询内置参数值报错或无内置参数[可忽略]，%s", responseEntity.getBody()));
        }
        SpecifiedParamsContext.setSpecifiedParamsResponse(responseEntity.getBody());
        log.info("SpecifiedParams: " + responseEntity.getBody());
        return responseEntity.getBody();
    }

    /**
     * 项目客制化需求，azkaban执行完毕后更新表里的内置参数值
     */
    public static void updateSpecifiedParams(Logger log, String workDir, Long tenantId, String jobName, Boolean success) {
        String hdspPropertiesPath = CommonUtil.getAzHomeByWorkDir(workDir) + "/conf/hdsp.properties";
        String hdspCoreUrl = ParamsUtil.getHdspCoreUrl(log, hdspPropertiesPath);
        try {
            SpecifiedParamsResponse specifiedParams = SpecifiedParamsContext.current();
            // 判断是否需要去更新 无内置参数可不更新
            if (Objects.nonNull(specifiedParams.getFailed())) {
                log.warn("内置参数查询错误或无内置参数值[可忽略]，无需更新");
                return;
            }
            log.info(String.format("context specifiedParams: %s", specifiedParams));
            HashMap<String, Object> body = new HashMap<>(8);
            body.put("tenantId", tenantId);
            body.put("timestampType", jobName);
            body.put("currentDateTime", specifiedParams.getCurrentDataTime());
            body.put("lastDateTime", specifiedParams.getLastDateTime());
            body.put("currentMaxId", specifiedParams.getCurrentMaxId());
            body.put("lastMaxId", specifiedParams.getLastMaxId());
            body.put("success", Optional.ofNullable(success).orElse(false));
            HttpEntity<String> requestEntity = new HttpEntity<>(GSON.toJson(body), RestTemplateUtil.httpHeaders());
            ResponseEntity<String> responseEntity = RESTTEMPLATE.postForEntity(
                    String.format("%s/v2/%d/timestamp-controls/update-increment", hdspCoreUrl, tenantId),
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
     * @param log     az log
     * @param str     替换的文本
     * @param workDir az执行目录
     * @param jobName az job_id
     * @return java.lang.String
     */
    public static String handlePredefinedParams(Logger log, String str, String workDir, String jobName) {
        Matcher matcher = PredefinedParams.PREDEFINED_PARAM_REGEX.matcher(str);
        SpecifiedParamsResponse specifiedParamsResponse = getSpecifiedParams(log, 0L, workDir, jobName);
        while (matcher.find()) {
            // _p_current_data_time
            if (matcher.group(1).trim().contains(PredefinedParams.CURRENT_DATE_TIME)) {
                str = handleDateTime(str, matcher.group(1).trim(), specifiedParamsResponse);
            }
            // _p_last_date_time
            if (matcher.group(1).trim().contains(PredefinedParams.LAST_DATE_TIME)) {
                str = str.replaceAll(String.format(PARAM_PATTERN, PredefinedParams.LAST_DATE_TIME),
                        specifiedParamsResponse.getLastDateTime());
            }
            // _p_last_date_time
            if (matcher.group(1).trim().contains(PredefinedParams.CURRENT_MAX_ID)) {
                str = str.replaceAll(String.format(PARAM_PATTERN, PredefinedParams.CURRENT_MAX_ID),
                        specifiedParamsResponse.getCurrentMaxId());
            }
            // _p_last_max_id
            if (matcher.group(1).trim().contains(PredefinedParams.LAST_MAX_ID)) {
                str = str.replaceAll(String.format(PARAM_PATTERN, PredefinedParams.LAST_MAX_ID),
                        specifiedParamsResponse.getLastMaxId());
            }
        }
        return str;
    }

    private static String handleDateTime(String str, String matcher, SpecifiedParamsResponse specifiedParamsResponse) {
        DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 表查不到当前时间取本地时间
        String originDataTime = Optional.ofNullable(specifiedParamsResponse.getCurrentDataTime())
                .orElse(LocalDateTime.now().format(defaultFormatter));
        String[] splitArr = matcher.split(PredefinedParams.SPLIT_KEY);
        int defaultSize = 2;
        String currentDateTime;
        if (splitArr.length > defaultSize) {
            //  _p_current_date_time:N:day
            currentDateTime = genLocalDateTime(originDataTime, defaultFormatter, Long.valueOf(splitArr[1]), splitArr[2]);
            str = str.replaceAll(String.format("\\$\\{%s\\:%s\\:%s\\}", splitArr[0], splitArr[1], splitArr[2]), currentDateTime);
        } else if (splitArr.length == defaultSize) {
            // _p_current_date_time:yyyy-MM-dd HH:mm:ss
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(splitArr[1]);
            currentDateTime = LocalDateTime.parse(originDataTime, formatter).format(formatter);
            str = str.replaceAll(String.format("\\$\\{%s\\:%s\\}", splitArr[0], splitArr[1]), currentDateTime);
        } else {
            // _p_current_date_time
            currentDateTime = LocalDateTime.parse(originDataTime, defaultFormatter).format(defaultFormatter);
            str = str.replaceAll(String.format(PARAM_PATTERN, splitArr[0]), currentDateTime);
        }
        // 时间戳的currentDateTime需更新
        Optional.ofNullable(SpecifiedParamsContext.current()).ifPresent(o ->
                o.setCurrentDataTime(LocalDateTime.parse(currentDateTime, defaultFormatter).format(defaultFormatter)));
        return str;
    }

    private static String genLocalDateTime(String originDataTime, DateTimeFormatter defaultFormatter, Long interval, String unit) {
        LocalDateTime localDateTime = LocalDateTime.parse(originDataTime, defaultFormatter);
        String currentDateTime;
        switch (SimpleTimeUnitEnum.valueOf(unit.toUpperCase())) {
            case YEAR:
                currentDateTime = localDateTime.plusYears(interval).format(defaultFormatter);
                break;
            case MONTH:
                currentDateTime = localDateTime.plusMonths(interval).format(defaultFormatter);
                break;
            case WEEK:
                currentDateTime = localDateTime.plusWeeks(interval).format(defaultFormatter);
                break;
            case DAY:
                currentDateTime = localDateTime.plusDays(interval).format(defaultFormatter);
                break;
            case HOUR:
                currentDateTime = localDateTime.plusHours(interval).format(defaultFormatter);
                break;
            case MIN:
                currentDateTime = localDateTime.plusMinutes(interval).format(defaultFormatter);
                break;
            case SEC:
                currentDateTime = localDateTime.plusSeconds(interval).format(defaultFormatter);
                break;
            default:
                throw new IllegalArgumentException(String.format("非法单位[%s]，单位[year，month，week，day，hour，min，sec]", unit));
        }
        return currentDateTime;
    }

}
