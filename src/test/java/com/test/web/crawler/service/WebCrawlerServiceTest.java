package com.test.web.crawler.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.test.web.crawler.model.UrlNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebCrawlerServiceTest {

    private WebCrawlerService webCrawlerService;

    @Mock
    private HtmlExtractorService htmlExtractorService;

    private String url = "http://someurl.com";
    private String title = "Root Title";
    private String title1 = "Title 1";
    private String url1 = "Url 1";
    private String title2 = "Title 2";
    private String url2 = "Url 2";
    private String title3 = "Title 3";
    private String url3 = "Url 3";
    private String title1_1 = "Title 1 --> 1";
    private String url1_1 = "Url 1 --> 1";
    private String title1_2 = "Title 1 --> 2";
    private String url1_2 = "Url 1 --> 2";
    private String title2_1 = "Title 2 --> 1";
    private String url2_1 = "Url 2 --> 1";
    private List<UrlNode> urlChild;

    @Before
    public void setup() throws MalformedURLException {
        webCrawlerService = new WebCrawlerServiceImpl(htmlExtractorService, 5);
        when(htmlExtractorService.extractTitle(url)).thenReturn(title);

        UrlNode urlNode1 = new UrlNode(url1, title1);
        UrlNode urlNode2 = new UrlNode(url2, title2);
        UrlNode urlNode3 = new UrlNode(url3, title3);

        urlChild = newArrayList(urlNode1, urlNode2, urlNode3);
        when(htmlExtractorService.extractAttributesWithTitle(url)).thenReturn(urlChild);

        UrlNode urlNode1_1 = new UrlNode(url1_1, title1_1);
        UrlNode urlNode1_2 = new UrlNode(url1_2, title1_2);
        List<UrlNode> urlChild1 = newArrayList(urlNode1_1, urlNode1_2);
        when(htmlExtractorService.extractAttributesWithTitle(url1)).thenReturn(urlChild1);

        UrlNode urlNode2_1 = new UrlNode(url2_1, title2_1);
        List<UrlNode> urlChild2 = newArrayList(urlNode2_1);
        when(htmlExtractorService.extractAttributesWithTitle(url2)).thenReturn(urlChild2);
    }

    /**
     * It should get all links in the url and go on collecting until none is found.
     */
    @Test
    public void testCrawling() throws JsonProcessingException {
        String urlNode = webCrawlerService.crawl(url, null);

        ReadContext ctx = JsonPath.parse(urlNode);
        assertThat((String) ctx.read("$.url")).isEqualTo(url);
        assertThat((String) ctx.read("$.title")).isEqualTo(title);
        assertThat((String) ctx.read("$.urlNodes[0].url")).isEqualTo(url1);
        assertThat((String) ctx.read("$.urlNodes[0].title")).isEqualTo(title1);
        assertThat((String) ctx.read("$.urlNodes[0].urlNodes[0].url")).isEqualTo(url1_1);
        assertThat((String) ctx.read("$.urlNodes[0].urlNodes[0].title")).isEqualTo(title1_1);
        assertThat((String) ctx.read("$.urlNodes[0].urlNodes[1].url")).isEqualTo(url1_2);
        assertThat((String) ctx.read("$.urlNodes[0].urlNodes[1].title")).isEqualTo(title1_2);
        assertThat((String) ctx.read("$.urlNodes[1].url")).isEqualTo(url2);
        assertThat((String) ctx.read("$.urlNodes[1].title")).isEqualTo(title2);
        assertThat((String) ctx.read("$.urlNodes[1].urlNodes[0].url")).isEqualTo(url2_1);
        assertThat((String) ctx.read("$.urlNodes[1].urlNodes[0].title")).isEqualTo(title2_1);
        assertThat((String) ctx.read("$.urlNodes[2].url")).isEqualTo(url3);
        assertThat((String) ctx.read("$.urlNodes[2].title")).isEqualTo(title3);
        assertThat((String) ctx.read("$.urlNodes[2].urlNodes")).isNull();
    }

    /**
     * It should get all links in the url and go on collecting until none is found and
     * It should handle the case when the url is already visited, make sure it never ends up in an infinite loop.
     */
    @Test
    public void testCrawlingWhenURLIsAlreadyVisited() throws JsonProcessingException {
        when(htmlExtractorService.extractAttributesWithTitle(url3)).thenReturn(urlChild);
        String urlNode = webCrawlerService.crawl(url, null);

        ReadContext ctx = JsonPath.parse(urlNode);

        assertThat((String) ctx.read("$.url")).isEqualTo(url);
        assertThat((String) ctx.read("$.title")).isEqualTo(title);
        assertThat((String) ctx.read("$.urlNodes[0].url")).isEqualTo(url1);
        assertThat((String) ctx.read("$.urlNodes[0].title")).isEqualTo(title1);
        assertThat((String) ctx.read("$.urlNodes[0].urlNodes[0].url")).isEqualTo(url1_1);
        assertThat((String) ctx.read("$.urlNodes[0].urlNodes[0].title")).isEqualTo(title1_1);
        assertThat((String) ctx.read("$.urlNodes[0].urlNodes[1].url")).isEqualTo(url1_2);
        assertThat((String) ctx.read("$.urlNodes[0].urlNodes[1].title")).isEqualTo(title1_2);
        assertThat((String) ctx.read("$.urlNodes[1].url")).isEqualTo(url2);
        assertThat((String) ctx.read("$.urlNodes[1].title")).isEqualTo(title2);
        assertThat((String) ctx.read("$.urlNodes[1].urlNodes[0].url")).isEqualTo(url2_1);
        assertThat((String) ctx.read("$.urlNodes[1].urlNodes[0].title")).isEqualTo(title2_1);
        assertThat((String) ctx.read("$.urlNodes[2].url")).isEqualTo(url3);
        assertThat((String) ctx.read("$.urlNodes[2].title")).isEqualTo(title3);
        assertThat((String) ctx.read("$.urlNodes[2].urlNodes")).isNull();
    }

    /**
     * It should get links in the url to max depth of 1.
     */
    @Test
    public void testCrawlingWithDepth2() throws JsonProcessingException {
        String urlNode = webCrawlerService.crawl(url, 1);

        ReadContext ctx = JsonPath.parse(urlNode);

        assertThat((String) ctx.read("$.url")).isEqualTo(url);
        assertThat((String) ctx.read("$.title")).isEqualTo(title);
        assertThat((String) ctx.read("$.urlNodes[0].url")).isEqualTo(url1);
        assertThat((String) ctx.read("$.urlNodes[0].title")).isEqualTo(title1);
        assertThat((String) ctx.read("$.urlNodes[0].urlNodes")).isNull();
        assertThat((String) ctx.read("$.urlNodes[1].url")).isEqualTo(url2);
        assertThat((String) ctx.read("$.urlNodes[1].title")).isEqualTo(title2);
        assertThat((String) ctx.read("$.urlNodes[1].urlNodes")).isNull();
        assertThat((String) ctx.read("$.urlNodes[2].url")).isEqualTo(url3);
        assertThat((String) ctx.read("$.urlNodes[2].title")).isEqualTo(title3);
        assertThat((String) ctx.read("$.urlNodes[2].urlNodes")).isNull();
    }

    /**
     * It should get links in the url to max depth of 0.
     */
    @Test
    public void testCrawlingWithDepth0() throws JsonProcessingException {
        String urlNode = webCrawlerService.crawl(url, 0);

        ReadContext ctx = JsonPath.parse(urlNode);

        assertThat((String) ctx.read("$.url")).isEqualTo(url);
        assertThat((String) ctx.read("$.title")).isEqualTo(title);
    }

    /**
     * It should return null when max depth is less than 0.
     */
    @Test
    public void testCrawlingWithDepthLessThan0() throws JsonProcessingException {
        assertThat(webCrawlerService.crawl(url, -12)).isNull();
    }
}