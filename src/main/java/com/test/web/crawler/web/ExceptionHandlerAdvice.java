package com.test.web.crawler.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.web.crawler.exception.ConnectException;
import com.test.web.crawler.web.validation.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public ExceptionHandlerAdvice(MessageSource messageSource) {
        checkNotNull(messageSource);

        this.messageSource = messageSource;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        log.info("Error with URL {} ", request.getRequestURL(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = JsonProcessingException.class)
    public ResponseEntity<String> jsonProcessingErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        log.info("Error with URL {} ", request.getRequestURL(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = ConnectException.class)
    public ResponseEntity<String> connectExceptionErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        log.info("Error with URL {} ", request.getRequestURL(), e);
        return new ResponseEntity<>("The input url cannot be connected.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationError error = fromBindingErrors(exception.getBindingResult());
        return new ResponseEntity<>(error, headers, status);
    }

    private ValidationError fromBindingErrors(Errors errors) {
        ValidationError error = new ValidationError("Validation failed. " + errors.getErrorCount() + " error(s)");
        for (ObjectError objectError : errors.getAllErrors()) {
            try {
                error.addValidationError(messageSource.getMessage(objectError, null));
            } catch (NoSuchMessageException ignored) {
                log.error("message not found : " + ignored);
            }
        }
        return error;
    }
}
