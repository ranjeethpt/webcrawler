package com.test.web.crawler.service;

import com.test.web.crawler.model.UrlNode;

import java.util.List;

public interface HtmlExtractorService {
    List<UrlNode> extractAttributesWithTitle(String url);

    String extractTitle(String url);
}
