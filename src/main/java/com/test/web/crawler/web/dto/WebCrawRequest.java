package com.test.web.crawler.web.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel(value = "WebCrawRequest")
public class WebCrawRequest {

    @NotBlank
    @ApiModelProperty(required = true, example = "http://google.com")
    private String url;

    @ApiModelProperty(example = "2")
    private Integer maxDepth;
}
