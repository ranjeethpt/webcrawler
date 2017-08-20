package com.test.web.crawler.service;

import com.test.web.crawler.exception.ConnectException;
import com.test.web.crawler.model.UrlNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.test.web.crawler.util.StringUtils.checkNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JsoupExtractorServiceImpl implements HtmlExtractorService {

    private final String cssQuery;
    private final String attributeKey;

    public JsoupExtractorServiceImpl(String cssQuery, String attributeKey) {
        checkNotBlank(cssQuery);
        checkNotBlank(attributeKey);

        this.attributeKey = attributeKey;
        this.cssQuery = cssQuery;
    }

    @Override
    @Cacheable("attribute-url")
    public List<UrlNode> extractAttributesWithTitle(String url) {
        checkNotBlank(url);

        Document document;
        try {
            document = connect(url);
        } catch (Exception e) {
            //Ignoring child connection exceptions.
            return null;
        }
        Elements elements = document.select(cssQuery);
        List<UrlNode> urlNodes = newArrayList();
        elements.forEach(element -> {
            String value = element.attr(attributeKey);
            if (isNotBlank(value)) {
                UrlNode urlNode = new UrlNode(value, element.attr("title"));
                urlNodes.add(urlNode);
            }
        });
        return urlNodes;
    }

    @Override
    @Cacheable("title-url")
    public String extractTitle(String url) {
        try {
            return connect(url).title();
        } catch (Exception e) {
            throw new ConnectException(e.getMessage());
        }
    }

    private Document connect(String url) throws Exception {
        return Jsoup.connect(url).get();
    }
}
