package org.abigballpfmud.azkaban.plugin.rest.model;

import java.io.Serializable;

import org.springframework.http.HttpHeaders;

/**
 * <p>
 * Http响应
 * </p>
 *
 * @author isacc 2020/4/24 13:48
 * @since 1.0
 */
public class HttpResp implements Serializable {

    private HttpHeaders headers;

    private String body;

    private String statusCode;

    private Integer statusCodeValue;

    public HttpResp() {
    }

    public HttpResp(HttpHeaders headers, String body, String statusCode, Integer statusCodeValue) {
        this.headers = headers;
        this.body = body;
        this.statusCode = statusCode;
        this.statusCodeValue = statusCodeValue;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getStatusCodeValue() {
        return statusCodeValue;
    }

    public void setStatusCodeValue(Integer statusCodeValue) {
        this.statusCodeValue = statusCodeValue;
    }

    @Override
    public String toString() {
        return "HttpResp{" +
                "headers=" + headers +
                ", body='" + body + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", statusCodeValue=" + statusCodeValue +
                '}';
    }
}
