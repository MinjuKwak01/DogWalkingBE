package com.kakaoseventeen.dogwalking._core.security;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.Proxy;
import java.net.InetSocketAddress;


/**
 * RestTemplateConfig -> Spring에서 지원하는 객체로 간편하게 Rest 방식 API를 호출할 수 있는 Spring 내장 클래스
 *
 * @author 이승건
 * @version 1.0
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    @Profile({"prod", "cloud"})
    public RestTemplate restTemplateForDeploy() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("krmp-proxy.9rum.cc", 3128));
        requestFactory.setProxy(proxy);

        return new RestTemplate(requestFactory);
    }


    @Bean
    @Profile("test")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}