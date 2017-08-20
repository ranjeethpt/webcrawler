package com.test.web.crawler.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.web.crawler.service.WebCrawlerService;
import com.test.web.crawler.web.annotation.ExposedApi;
import com.test.web.crawler.web.dto.WebCrawRequest;
import com.test.web.crawler.web.validation.URLValidator;
import com.test.web.crawler.web.validation.ValidationError;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@ExposedApi
@RestController
@RequestMapping("/services")
public class WebCrawlerController {

    private final WebCrawlerService webCrawlerService;

    public WebCrawlerController(WebCrawlerService webCrawlerService) {
        checkNotNull(webCrawlerService);

        this.webCrawlerService = webCrawlerService;
    }

    @ApiOperation(value = "Crawl URL", notes = "Crawls deeply...")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Crawled deeply!!!"),
            @ApiResponse(code = 400, message = "Input data in invalid!!!", response = ValidationError.class)
    })
    @RequestMapping(value = "/crawl", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> crawlUrl(@RequestBody @Valid WebCrawRequest webCrawRequest) throws JsonProcessingException {
        return new ResponseEntity<>(webCrawlerService.crawl(webCrawRequest.getUrl(), webCrawRequest.getMaxDepth()), HttpStatus.OK);
    }

    @InitBinder
    protected void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new URLValidator());
    }
}
