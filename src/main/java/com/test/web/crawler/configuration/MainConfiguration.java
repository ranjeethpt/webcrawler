package com.test.web.crawler.configuration;

import com.test.web.crawler.service.HtmlExtractorService;
import com.test.web.crawler.service.JsoupExtractorServiceImpl;
import com.test.web.crawler.service.WebCrawlerService;
import com.test.web.crawler.service.WebCrawlerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfiguration {

    @Bean
    public HtmlExtractorService jsoupExtractorService() {
        return new JsoupExtractorServiceImpl("a[href]", "abs:href");
    }

    @Bean
    public WebCrawlerService webCrawlerService(HtmlExtractorService htmlExtractorService) {
        return new WebCrawlerServiceImpl(htmlExtractorService, 3);
    }
}
