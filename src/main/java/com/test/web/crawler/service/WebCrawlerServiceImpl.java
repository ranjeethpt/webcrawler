package com.test.web.crawler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.web.crawler.model.UrlNode;
import com.test.web.crawler.util.json.JsonConverter;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;

/**
 *
 */
public class WebCrawlerServiceImpl implements WebCrawlerService {

    private HashSet<String> visitedLinks;
    private final Integer defaultMaxDepth;

    private final HtmlExtractorService htmlExtractorService;

    public WebCrawlerServiceImpl(HtmlExtractorService htmlExtractorService, Integer defaultMaxDepth) {
        checkNotNull(htmlExtractorService);
        checkNotNull(defaultMaxDepth);

        this.htmlExtractorService = htmlExtractorService;
        this.defaultMaxDepth = defaultMaxDepth;
    }

    @Override
    public String crawl(String url, Integer maxDepth) throws JsonProcessingException {
        checkNotNull(url, "Expecting a url in string format, it cannot be null.");

        if (maxDepth == null) {
            maxDepth = defaultMaxDepth;
        }
        if (maxDepth < 0) {
            return null;
        }
        visitedLinks = newHashSet();
        UrlNode rootNode = new UrlNode(url, htmlExtractorService.extractTitle(url));
        return JsonConverter.getInstance().writeValueAsString(getPageLinks(url, 0, maxDepth, rootNode));
    }

    private UrlNode getPageLinks(String url, Integer start, Integer maxDepth, UrlNode node) {
        if (!visitedLinks.contains(url) && !Objects.equals(start, maxDepth)) {
            List<UrlNode> urls = htmlExtractorService.extractAttributesWithTitle(url);
            visitedLinks.add(url);
            start++;
            if (urls != null) {
                for (UrlNode urlNode : urls) {
                    if (!visitedLinks.contains(urlNode.getUrl())) {
                        node.addChild(urlNode);
                        getPageLinks(urlNode.getUrl(), start, maxDepth, urlNode);
                    }
                }
            }
        }
        return node;
    }
}
