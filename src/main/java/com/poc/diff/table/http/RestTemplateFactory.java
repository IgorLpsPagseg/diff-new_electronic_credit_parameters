package com.poc.diff.table.http;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFactory {
    private static RestTemplate restTemplate;

    public static RestTemplate getRestTemplate() {
        if (RestTemplateFactory.restTemplate != null) {
            return restTemplate;
        }
        return createRestTemplate();
    }

    private static RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        setRestTemplate(restTemplate);
        return restTemplate;
    }

    public static void setRestTemplate(RestTemplate restTemplate) {
        RestTemplateFactory.restTemplate = restTemplate;
    }

    public HttpHeaders getHeadersSetToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        headers.set("token", token);
        return headers;
    }

    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        return headers;
    }
}