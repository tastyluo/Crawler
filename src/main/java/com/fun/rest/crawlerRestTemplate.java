package com.fun.rest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-06-30 16:20
 */
@Component
public class crawlerRestTemplate extends RestTemplate {

    private static Logger logger = Logger.getLogger(crawlerRestTemplate.class);

    private final static String CONTENT_TYPE = "application/json; charset=UTF-8";

    private HttpEntity<String> setDefaultHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", CONTENT_TYPE);
        return new HttpEntity<String>(headers);
    }

    public <T> T getForCObject(String url, Class<T> responseType, Object... urlVariables) throws RestClientException {
        return super.getForObject(url, responseType, urlVariables);
    }

    public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> urlVariables) throws RestClientException {
        return super.getForObject(url, responseType, urlVariables);
    }

    public <T> T getForObject(URI url, Class<T> responseType) throws RestClientException {
        return super.getForObject(url, responseType);
    }

    public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        return super.postForObject(url, request, responseType, uriVariables);
    }

    public <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        return super.postForObject(url, request, responseType, uriVariables);
    }

    public <T> T postForObject(URI url, Object request, Class<T> responseType) throws RestClientException {
        return super.postForObject(url, request, responseType);
    }

    public void test () {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);// 设置超时
        requestFactory.setReadTimeout(1000);

        //利用复杂构造器可以实现超时设置，内部实际实现为 HttpClient
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        //设置HTTP请求头信息，实现编码等
        HttpHeaders requestHeaders = new HttpHeaders();
        // requestHeaders.set("Accept", "text/");
        requestHeaders.set("Accept-Charset", "utf-8");
        requestHeaders.set("Content-type", "text/xml; charset=utf-8");// 设置编码

        //利用容器实现数据封装，发送
        HttpEntity<String> entity = new HttpEntity<>(requestHeaders);
//        restTemplate.getForObject("", entity, String.class);
    }
}
