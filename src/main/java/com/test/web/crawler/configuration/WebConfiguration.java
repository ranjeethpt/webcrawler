package com.test.web.crawler.configuration;

import com.test.web.crawler.service.WebCrawlerService;
import com.test.web.crawler.web.ExceptionHandlerAdvice;
import com.test.web.crawler.web.controller.WebCrawlerController;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class WebConfiguration {
    @Bean
    public WebCrawlerController webCrawler(WebCrawlerService webCrawlerService) {
        return new WebCrawlerController(webCrawlerService);
    }

    @Bean
    public ExceptionHandlerAdvice exceptionHandlerAdvice(MessageSource messageSource) {
        return new ExceptionHandlerAdvice(messageSource);
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }
}
