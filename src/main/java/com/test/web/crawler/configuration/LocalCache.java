package com.test.web.crawler.configuration;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.test.web.crawler.configuration.LocalCache.PROFILE;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Profile(PROFILE)
public @interface LocalCache {
    String PROFILE = "Local-Cache";
}