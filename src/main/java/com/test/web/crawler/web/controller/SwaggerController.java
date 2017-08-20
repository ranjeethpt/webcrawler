package com.test.web.crawler.web.controller;

import io.swagger.models.Swagger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by denisssudak on 24/10/16.
 *
 * @author denisssudak
 */
@Controller
public class SwaggerController {

    private static final String HAL_MEDIA_TYPE = "application/hal+json";

    private final DocumentationCache documentationCache;

    private final ServiceModelToSwagger2Mapper mapper;

    private final JsonSerializer jsonSerializer;

    public SwaggerController(DocumentationCache documentationCache, ServiceModelToSwagger2Mapper mapper, JsonSerializer jsonSerializer) {
        this.documentationCache = documentationCache;
        this.mapper = mapper;
        this.jsonSerializer = jsonSerializer;
    }

    @RequestMapping(value = "/api-docs", method = RequestMethod.GET, produces = {APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE})
    public
    @ResponseBody
    ResponseEntity<Json> getDocumentation(@RequestParam(value = "group", required = false) String swaggerGroup) {
        String groupName = Optional.ofNullable(swaggerGroup).orElse(Docket.DEFAULT_GROUP_NAME);
        Documentation documentation = documentationCache.documentationByGroup(groupName);
        if (documentation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Swagger swagger = mapper.mapDocumentation(documentation);

        return new ResponseEntity<>(jsonSerializer.toJson(swagger), HttpStatus.OK);
    }

}
