package com.test.web.crawler.web;

import com.test.web.crawler.service.WebCrawlerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class TestContextConfiguration {

    @Bean
    public WebCrawlerService webCrawlerService() {
        return mock(WebCrawlerService.class);
    }
}
