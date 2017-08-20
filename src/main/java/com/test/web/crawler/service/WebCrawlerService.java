package com.test.web.crawler.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.net.URL;

public interface WebCrawlerService {
    String crawl(String url, Integer maxDepth) throws JsonProcessingException;
}
