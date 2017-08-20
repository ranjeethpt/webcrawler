package com.test.web.crawler.web.validation;

import com.test.web.crawler.web.dto.WebCrawRequest;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class URLValidator implements Validator {

    private static final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

    public boolean supports(Class<?> aClass) {
        return WebCrawRequest.class.isAssignableFrom(aClass);
    }

    public void validate(Object o, Errors errors) {
        if (!errors.hasErrors()) {
            WebCrawRequest webCrawRequest = (WebCrawRequest) o;
            if (!urlValidator.isValid(webCrawRequest.getUrl())) {
                errors.rejectValue("url", "invalid");
            }
        }
    }
}
