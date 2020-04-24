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

    public static final String METHOD = "method";
    public static final String AUTH = "auth";
    public static final String GRANT_TYPE = "grantType";
    public static final String TOKEN_URI = "tokenUri";
    public static final String CLIENT_ID = "clientId";
    public static final String CLIENT_SECRET = "clientSecret";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String REQUEST = "request";
    public static final String EXPRESSION = "expression";
    public static final String CONTENT_TYPE = "contentType";

    public static final String HEADER = "header";
    public static final String QUERY = "query";
    public static final String BODY = "body";

    public static final String QUESTION = "?";
    public static final String AND = "&";

    public static final String HTTP_SOCKET_TIMEOUT = "http.socketTimeout";
    public static final String HTTP_CONNECTION_TIMEOUT = "http.connectTimeout";
    public static final String HTTP_REQUEST_TIMEOUT = "http.requestTimeout";
    public static final String HTTP_MAX_REDIRECTS = "http.maxRedirects";
    public static final String POOL_MAX_TOTAL = "pool.maxTotal";
    public static final String POOL_MAX_PER_ROUTE = "pool.maxPerRoute";

    public static final String DEFAULT_HTTP_SOCKET_TIMEOUT = "10000";
    public static final String DEFAULT_HTTP_CONNECTION_TIMEOUT = "5000";
    public static final String DEFAULT_HTTP_REQUEST_TIMEOUT = "1000";
    public static final String DEFAULT_HTTP_MAX_REDIRECTS = "5";
    public static final String DEFAULT_POOL_MAX_TOTAL = "8";
    public static final String DEFAULT_POOL_MAX_PER_ROUTE = "2";

}
