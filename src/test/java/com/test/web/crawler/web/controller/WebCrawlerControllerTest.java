package com.test.web.crawler.web.controller;


import com.test.web.crawler.configuration.WebConfiguration;
import com.test.web.crawler.service.WebCrawlerService;
import com.test.web.crawler.web.TestContextConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContextConfiguration.class, WebConfiguration.class})
@WebAppConfiguration
public class WebCrawlerControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private WebCrawlerService webCrawlerService;

    @Before
    public void setUp() {
        reset(webCrawlerService);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     * It should process valid request with only url and return json.
     *
     * @throws Exception
     */
    @Test
    public void testCrawlUrlValidURL() throws Exception {
        String url = "http://www.google.com";
        String result = "{result}";
        String inputJson = "{\"url\":\"" + url + "\"}";
        when(webCrawlerService.crawl(url, null)).thenReturn(result);

        mockMvc.perform(post("/services/crawl")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(inputJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string("{result}"));
    }

    /**
     * It should process valid request with  url & max depth and then return json.
     *
     * @throws Exception
     */
    @Test
    public void testCrawlUrlValidURLAndMaxDepth() throws Exception {
        String url = "http://www.google.com";
        Integer maxDepth = 6;
        String result = "{result}";
        String inputJson = "{\"url\":\"" + url + "\"" + ",\"maxDepth\":" + maxDepth + "}";
        when(webCrawlerService.crawl(url, maxDepth)).thenReturn(result);

        mockMvc.perform(post("/services/crawl")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(inputJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string("{result}"));
    }

    /**
     * It should respond with bad request and the response should have a proper validation message.
     */
    @Test
    public void testCrawlUrlInValidURL() throws Exception {
        mockMvc.perform(post("/services/crawl")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{\"url\":\"bla bla bla\"}\n"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.errorMessage", is("Validation failed. 1 error(s)")))
                .andExpect(jsonPath("$.errors[0]", is("Invalid url format!!")));
    }

}
