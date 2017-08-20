package com.test.web.crawler.configuration;


import com.fasterxml.classmate.TypeResolver;
import com.test.web.crawler.web.annotation.ExposedApi;
import com.test.web.crawler.web.controller.SwaggerController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static springfox.documentation.builders.RequestHandlerSelectors.withClassAnnotation;
import static springfox.documentation.schema.AlternateTypeRules.newRule;


@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket publicApi() {
        TypeResolver resolver = new TypeResolver();
        AlternateTypeRule collectionRule
                = newRule(
                //replace Collection<T> for any T
                resolver.resolve(Collection.class, WildcardType.class),
                //with List<T> for any T
                resolver.resolve(List.class, WildcardType.class));

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(withClassAnnotation(ExposedApi.class))
                .build()
                .directModelSubstitute(LocalDate.class, String.class)
                .alternateTypeRules(collectionRule)
                .pathProvider(new BasePathAwareRelativePathProvider("/services/reports"))// https://github.com/springfox/springfox/issues/963
                .apiInfo(apiInfo());
    }

    @Bean
    public ApiInfo apiInfo() {
        String title = "Web Crawler";
        String description = "API for deep web crawling";
        String version = "1.0";
        String termsOfServiceURL = "";
        Contact contact = new Contact("Ranjeeth Padinhare Thattariyil", "https://www.linkedin.com/in/ranjeethpt/", "ranjeeth.pt@gmail.com");
        String licence = "";
        String licenceURL = "";
        return new ApiInfo(title, description, version, termsOfServiceURL, contact, licence, licenceURL);
    }

    @Bean
    public SwaggerController swagger2Controller(DocumentationCache documentationCache, ServiceModelToSwagger2Mapper mapper, JsonSerializer jsonSerializer) {
        return new SwaggerController(documentationCache, mapper, jsonSerializer);
    }


    private class BasePathAwareRelativePathProvider extends AbstractPathProvider {
        private String basePath;

        public BasePathAwareRelativePathProvider(String basePath) {
            this.basePath = basePath;
        }

        @Override
        protected String applicationPath() {
            return basePath;
        }

        @Override
        protected String getDocumentationPath() {
            return "/";
        }

        @Override
        public String getOperationPath(String operationPath) {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
            return Paths.removeAdjacentForwardSlashes(
                    uriComponentsBuilder.path(operationPath.replaceFirst(basePath, "")).build().toString());
        }
    }
}
