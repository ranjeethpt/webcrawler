package com.test.web.crawler.web.validation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationError {

    private final String errorMessage;
    private List<String> errors = new ArrayList<>();

    public void addValidationError(String error) {
        errors.add(error);
    }
}
