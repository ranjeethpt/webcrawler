package com.test.web.crawler.service;


import com.test.web.crawler.configuration.CacheConfiguration;
import com.test.web.crawler.exception.ConnectException;
import com.test.web.crawler.model.UrlNode;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@ActiveProfiles("Local-Cache")
@RunWith(PowerMockRunner.class)
@PrepareForTest(Jsoup.class)
@ContextConfiguration(classes = CacheConfiguration.class)
public class JsoupExtractorServiceImplTest {

    private HtmlExtractorService htmlExtractorService;
    private String cssQuery = "some query";
    private String attributeKey = "some attr key";

    private String url = "http://somurl.com";

    @Mock
    private Document document;

    @Mock
    private Connection connection;
    @Mock
    private Element elements1, elements2;

    @Before
    public void setup() throws IOException {
        mockStatic(Jsoup.class);

        htmlExtractorService = new JsoupExtractorServiceImpl(cssQuery, attributeKey);
        when(Jsoup.connect(url)).thenReturn(connection);
        when(connection.get()).thenReturn(document);
    }

    /**
     * It should connect to the url using Jsoup and return the title of the Document.
     */
    @Test
    public void testExtractTitle() {
        String title = "Title Bla Bla BLa";
        when(document.title()).thenReturn(title);

        assertThat(htmlExtractorService.extractTitle(url)).isNotNull().isEqualTo(title);
    }

    /**
     * It should connect to the url using Jsoup
     * and return a {@link UrlNode} having value as filtered by attributeKey and Title as filtered by Title .
     */
    @Test
    public void testExtractAttributesWithTitle() {
        String attrValue1 = "Attr Value 1";
        String titleValue1 = "Title 1";
        String attrValue2 = "Attr Value 2";
        String titleValue2 = "Title 2";

        Elements elements = new Elements();
        elements.add(elements1);
        elements.add(elements2);

        when(document.select(cssQuery)).thenReturn(elements);
        when(elements1.attr(attributeKey)).thenReturn(attrValue1);
        when(elements1.attr("title")).thenReturn(titleValue1);
        when(elements2.attr(attributeKey)).thenReturn(attrValue2);
        when(elements2.attr("title")).thenReturn(titleValue2);

        List<UrlNode> urlNodes = htmlExtractorService.extractAttributesWithTitle(url);
        assertThat(urlNodes).isNotNull().isNotEmpty().hasSize(2);
        assertThat(urlNodes.get(0)).isNotNull();
        assertThat(urlNodes.get(0).getUrl()).isEqualTo(attrValue1);
        assertThat(urlNodes.get(0).getTitle()).isEqualTo(titleValue1);
        assertThat(urlNodes.get(1)).isNotNull();
        assertThat(urlNodes.get(1).getUrl()).isEqualTo(attrValue2);
        assertThat(urlNodes.get(1).getTitle()).isEqualTo(titleValue2);
    }

    /**
     * It should return null on any exception while connecting.
     */
    @Test
    public void testExtractAttributesWithTitleOnException() {
        when(Jsoup.connect(url)).thenThrow(new RuntimeException());
        assertThat(htmlExtractorService.extractAttributesWithTitle(url)).isNull();
    }

    /**
     * It should throw {@link ConnectException} when any exception occurs.
     */
    @Test(expected = ConnectException.class)
    public void testExtractTitleConnectException() {
        when(Jsoup.connect(url)).thenThrow(new RuntimeException());
        htmlExtractorService.extractTitle(url);
    }
}
