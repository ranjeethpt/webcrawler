package com.test.web.crawler.model;


import lombok.Data;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

@Data
public class UrlNode {
    private final String url;
    private final String title;
    private List<UrlNode> urlNodes;


    public UrlNode(String url, String title) {
        checkNotNull(url);

        this.url = url;
        this.title = title;
    }

    public void addChild(UrlNode urlNode) {
        checkNotNull(urlNode);
        if (urlNodes == null) {
            urlNodes = newArrayList();
        }
        urlNodes.add(urlNode);
    }
}