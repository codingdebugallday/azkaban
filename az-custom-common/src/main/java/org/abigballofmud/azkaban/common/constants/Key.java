package org.abigballofmud.azkaban.common.constants;

/**
 * <p>
 * 配置常量
 * </p>
 *
 * @author isacc 2020/4/24 13:43
 * @since 1.0
 */
public class Key {

    private Key() {
    }

    public static final String PREFIX = "rest.";
    public static final String EMPTY_STRING = "null";

    /**
     * 接口来自平台内部还是外部，默认外部
     * api重试，默认不启用
     * 默认重试3次，重试间隔1秒，默认指数递增
     */
    public static final String EXTERNAL = PREFIX + "external";
    public static final String ENABLED_RETRY = PREFIX + "retry.enabled";
    public static final String RETRY_NUMBER = PREFIX + "retry.number";
    public static final String RETRY_INTERVAL = PREFIX + "retry.interval";
    public static final String  ENABLED_RETRY_EXPONENTIAL = PREFIX + "retry.exponential";
    /**
     * 如异步接口，需获取接口是否执行完毕，故需提供一个查询接口执行状态的接口
     * 若接口还未执行完成，隔多长时间再次调用查询是否执行完毕
     * 单位 秒，默认间隔5秒，默认指数递增
     * 最多查询几次，默认3次
     * 查询3次后，若还是running或failed则默认结束，az job状态为success
     */
    public static final String ENABLED_CALLBACK = PREFIX + "callback.enabled";
    public static final String CALLBACK_URI = PREFIX + "callback.uri";
    public static final String CALLBACK_USE_GATEWAY = PREFIX + "callback.useGateway";
    public static final String CALLBACK_APP = PREFIX + "callback.app";
    public static final String CALLBACK_INTERVAL = PREFIX + "callback.interval";
    public static final String CALLBACK_NUMBER = PREFIX + "callback.number";
    public static final String ENABLED_CALLBACK_EXPONENTIAL = PREFIX +"callback.exponential";
    public static final String CALLBACK_FINISH_SUCCESS = PREFIX +"callback.finishSuccess";

    public static final String USE_GATEWAY = PREFIX + "useGateway";
    public static final String APP = PREFIX + "app";
    public static final String METHOD = PREFIX + "method";
    public static final String AUTH = PREFIX + "auth";
    public static final String GRANT_TYPE = PREFIX + "grantType";
    public static final String TOKEN_URI = PREFIX + "tokenUri";
    public static final String CLIENT_ID = PREFIX + "clientId";
    public static final String CLIENT_SECRET = PREFIX + "clientSecret";
    public static final String USERNAME = PREFIX + "username";
    public static final String PASSWORD = PREFIX + "password";
    public static final String REQUEST = PREFIX + "request";
    public static final String URI = PREFIX + "uri";
    public static final String CONTENT_TYPE = PREFIX + "contentType";

    public static final String HEADER = PREFIX + "header";
    public static final String QUERY = PREFIX + "query";
    public static final String BODY = PREFIX + "body";

    public static final String QUESTION = "?";
    public static final String AND = "&";

    public static final String HTTP_SOCKET_TIMEOUT = PREFIX + "http.socketTimeout";
    public static final String HTTP_CONNECTION_TIMEOUT = PREFIX + "http.connectTimeout";
    public static final String HTTP_REQUEST_TIMEOUT = PREFIX + "http.requestTimeout";
    public static final String HTTP_MAX_REDIRECTS = PREFIX + "http.maxRedirects";
    public static final String POOL_MAX_TOTAL = PREFIX + "pool.maxTotal";
    public static final String POOL_MAX_PER_ROUTE = PREFIX + "pool.maxPerRoute";

    public static final String DEFAULT_HTTP_SOCKET_TIMEOUT = "10000";
    public static final String DEFAULT_HTTP_CONNECTION_TIMEOUT = "5000";
    public static final String DEFAULT_HTTP_REQUEST_TIMEOUT = "1000";
    public static final String DEFAULT_HTTP_MAX_REDIRECTS = "5";
    public static final String DEFAULT_POOL_MAX_TOTAL = "8";
    public static final String DEFAULT_POOL_MAX_PER_ROUTE = "2";

}
