package com.test.web.crawler.web.validation;

import com.test.web.crawler.web.dto.WebCrawRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class URLValidatorTest {

    private Validator urlValidator;

    @Mock
    private Errors errors;

    private WebCrawRequest webCrawRequest;

    @Before
    public void setUp() {
        webCrawRequest = new WebCrawRequest();
        urlValidator = new URLValidator();

    }

    /**
     * It should support String class
     */
    @Test
    public void supports() {
        assertThat(urlValidator.supports(WebCrawRequest.class)).isTrue();
        assertThat(urlValidator.supports(BigDecimal.class)).isFalse();
    }

    /**
     * It should support valid http url.
     */
    @Test
    public void validateHttp() {
        webCrawRequest.setUrl("http://ranjeeth.com");
        urlValidator.validate(webCrawRequest, errors);
        verify(errors, never()).rejectValue(anyString(), anyString());
    }

    /**
     * It should support valid https url.
     */
    @Test
    public void validateHttps() {
        webCrawRequest.setUrl("https://ranjeeth.com");
        urlValidator.validate(webCrawRequest, errors);
        verify(errors, never()).rejectValue(anyString(), anyString());
    }

    /**
     * It should not support valid ftp url.
     */
    @Test
    public void validateFtp() {
        webCrawRequest.setUrl("ftp://ranjeeth.com");
        urlValidator.validate(webCrawRequest, errors);
        verify(errors).rejectValue("url", "invalid");
    }

    /**
     * It should support valid url.
     */
    @Test
    public void testInValidURLFormat() {
        webCrawRequest.setUrl("bla bla bla");
        urlValidator.validate(webCrawRequest, errors);
        verify(errors).rejectValue("url", "invalid");
    }

}