package org.abigballpfmud.azkaban.plugin.rest.auth;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import azkaban.utils.Props;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.abigballofmud.azkaban.common.exception.CustomerRuntimeException;
import org.abigballpfmud.azkaban.plugin.rest.constants.Key;
import org.abigballpfmud.azkaban.plugin.rest.model.AccessToken;
import org.abigballpfmud.azkaban.plugin.rest.utils.ChangeHttpRequest;
import org.abigballpfmud.azkaban.plugin.rest.utils.Conf;
import org.abigballpfmud.azkaban.plugin.rest.utils.Retry;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * OAuth2认证
 * </p>
 *
 * @author isacc 2020/4/24 13:45
 * @since 1.0
 */
public class OAuth2AuthProvider implements AuthProvider {

    private GrantType grantType;

    @Override
    public void provide(RestTemplate restTemplate, Props props, Logger log) {
        check(props);
        log.info("OAuth2 auth");
        grantType.authProvider().provide(restTemplate, props, log);
    }

    private void check(Props props) {
        // 验证method
        try {
            this.grantType = GrantType.valueOf(Conf.require(props, Key.GRANT_TYPE));
        } catch (Exception e) {
            throw new CustomerRuntimeException("Properties [" + Key.GRANT_TYPE + "] invalid");
        }
    }

}

/**
 * 授权类型
 */
enum GrantType {

    /**
     * 客户端授权
     */
    CLIENT_CREDENTIALS,

    /**
     * 密码授权
     */
    PASSWORD,

    ;

    public AuthProvider authProvider() {
        switch (this) {
            case CLIENT_CREDENTIALS:
                return new OAuth2ClientCredentialsAuthProvider();
            case PASSWORD:
                return new OAuth2PasswordAuthProvider();
            default:
                // never
                throw new CustomerRuntimeException("Need AuthProvider");
        }
    }

}

/**
 * 客户端
 */
class OAuth2ClientCredentialsAuthProvider implements AuthProvider {

    private static final String KEY = "ACCESS_TOKEN";
    private static final String TOKEN_URI_FMT = "%s?grant_type=client_credentials&client_id=%s&client_secret=%s";
    private static final int TOKEN_MIN_EXPIRED_SECONDS = 10 * 60;

    private final RestTemplate tokenRestTemplate;

    private Cache<String, AccessToken> cache;

    private Logger log;

    private String tokenUri;
    private String clientId;
    private String clientSecret;

    OAuth2ClientCredentialsAuthProvider() {
        this.tokenRestTemplate = new RestTemplate();
    }

    @Override
    public void provide(RestTemplate restTemplate, Props props, Logger log) {
        this.log=log;
        log.info("OAuth2 auth, grant_type: client_credentials");
        this.tokenUri = Conf.require(props, Key.TOKEN_URI);
        this.clientId = Conf.require(props, Key.CLIENT_ID);
        this.clientSecret = Conf.require(props, Key.CLIENT_SECRET);
        initCache();
        log.info("Add access_token Interceptor");
        // 拦截器
        restTemplate.getInterceptors().add((request, body, execution) -> {
            AccessToken accessToken;
            try {
                accessToken = cache.get(KEY, this::login);
            } catch (ExecutionException e) {
                throw new CustomerRuntimeException("Fetch access_token failed", e);
            }
            if (accessToken.getTokenType() == null) {
                String token = "access_token=" + accessToken.getAccessToken();
                request = new ChangeHttpRequest(request, token);
                log.info(String.format("Add http uri [%s]", token));
            } else {
                String token = accessToken.getTokenType() + " " + accessToken.getAccessToken();
                request.getHeaders().add("Authorization", token);
                log.info(String.format("Add http header [Authorization=%s]", token));
            }
            ClientHttpResponse response = execution.execute(request, body);
            // token失效了，需要重新刷新
            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.warn("Unauthorized，discard access_token");
                cache.invalidate(KEY);
            }
            return response;
        });
    }

    private void initCache() {
        AccessToken accessToken = login();
        int expired = accessToken.getExpiresIn();
        expired = expired > 0 ? expired : TOKEN_MIN_EXPIRED_SECONDS;
        log.info(String.format("Cache expired seconds: %d", expired));
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(1)
                .expireAfterAccess(expired, TimeUnit.SECONDS)
                .build();
        cache.put(KEY, accessToken);
        log.info("Cache access_token init");
    }

    private AccessToken login() {
        return Retry.of().doRetry(() -> {
            String uri = String.format(TOKEN_URI_FMT, tokenUri, clientId, clientSecret);
            log.info(String.format("Token uri: %s", uri));
            AccessToken accessToken = tokenRestTemplate.postForObject(uri, null, AccessToken.class);
            if (accessToken == null) {
                throw new CustomerRuntimeException("Fetch access_token failed, no content");
            }
            log.info(String.format("Fetch access_token: %s", accessToken));
            return accessToken;
        });
    }

}

/**
 * 密码
 */
class OAuth2PasswordAuthProvider implements AuthProvider {

    private static final String KEY = "ACCESS_TOKEN";
    private static final String TOKEN_URI_FMT = "%s?grant_type=password&client_id=%s&client_secret=%s&username=%s&password=%s";
    private static final int TOKEN_MIN_EXPIRED_SECONDS = 10 * 60;

    private final RestTemplate tokenRestTemplate;

    private Cache<String, AccessToken> cache;

    private Logger log;
    private String tokenUri;
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;

    OAuth2PasswordAuthProvider() {
        this.tokenRestTemplate = new RestTemplate();
    }

    @Override
    public void provide(RestTemplate restTemplate, Props props, Logger log) {
        this.log = log;
        log.info("OAuth2 auth, grant_type: password");
        this.tokenUri = Conf.require(props, Key.TOKEN_URI);
        this.clientId = Conf.require(props, Key.CLIENT_ID);
        this.clientSecret = Conf.require(props, Key.CLIENT_SECRET);
        this.username = Conf.require(props, Key.USERNAME);
        this.password = Conf.require(props, Key.PASSWORD);
        initCache();
        log.info("Add access_token Interceptor");
        // 拦截器
        restTemplate.getInterceptors().add((request, body, execution) -> {
            AccessToken accessToken;
            try {
                accessToken = cache.get(KEY, this::login);
            } catch (ExecutionException e) {
                throw new CustomerRuntimeException("Fetch access_token failed", e);
            }
            // 如果TokenType不存在则放入到url，否则加入请求头
            if (accessToken.getTokenType() == null) {
                String token = "access_token=" + accessToken.getAccessToken();
                request = new ChangeHttpRequest(request, token);
                log.info(String.format("Add http uri [%s]", token));
            } else {
                String token = accessToken.getTokenType() + " " + accessToken.getAccessToken();
                request.getHeaders().add("Authorization", token);
                log.info(String.format("Add http header [Authorization=%s]", token));
            }
            ClientHttpResponse response = execution.execute(request, body);
            // token失效了，需要重新刷新
            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.warn("Unauthorized，discard access_token");
                cache.invalidate(KEY);
            }
            return response;
        });
    }

    private void initCache() {
        AccessToken accessToken = login();
        int expired = accessToken.getExpiresIn();
        expired = expired > 0 ? expired : TOKEN_MIN_EXPIRED_SECONDS;
        log.info(String.format("Cache expired seconds: %d", expired));
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(1)
                .expireAfterAccess(expired, TimeUnit.SECONDS)
                .build();
        cache.put(KEY, accessToken);
        log.info("Cache access_token init");
    }

    private AccessToken login() {
        return Retry.of().doRetry(() -> {
            String uri = String.format(TOKEN_URI_FMT, tokenUri, clientId, clientSecret, username, password);
            log.info(String.format("Token uri: %s", uri));
            AccessToken accessToken = tokenRestTemplate.postForObject(uri, null, AccessToken.class);
            if (accessToken == null) {
                throw new CustomerRuntimeException("Fetch access_token failed, no content");
            }
            log.info(String.format("Fetch access_token: %s", accessToken));
            return accessToken;
        });
    }

}
