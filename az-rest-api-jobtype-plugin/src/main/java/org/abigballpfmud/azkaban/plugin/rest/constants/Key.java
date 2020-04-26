package org.abigballpfmud.azkaban.plugin.rest.constants;

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

    public static final String METHOD = PREFIX + "method";
    public static final String AUTH = PREFIX + "auth";
    public static final String GRANT_TYPE = PREFIX + "grantType";
    public static final String TOKEN_URI = PREFIX + "tokenUri";
    public static final String CLIENT_ID = PREFIX + "clientId";
    public static final String CLIENT_SECRET = PREFIX + "clientSecret";
    public static final String USERNAME = PREFIX + "username";
    public static final String PASSWORD = PREFIX + "password";
    public static final String REQUEST = PREFIX + "request";
    public static final String EXPRESSION = PREFIX + "expression";
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
