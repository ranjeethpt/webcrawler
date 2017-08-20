#Web crawler

##Introduction
The Api takes a URL and responds with a tree of child pages linked to the URL with deep crawling, the default max depth is 3. The max depth can be configured by passing an argument.

##Technology Stack
Its a Java 8 Spring based project, [Jsoup](https://jsoup.org/) is used for connecting to URL and extracting html contents.
The application uses **Mockito** as testing framework. The application has some Caching features and is enabled by using Profiles. Currently a *Local-cache* is present. The framework can be leveraged to use other caching frameworks like Redis or Elasticache.
The API checks for valid URL format, it supports http and https for invalid formats a proper validation message is responded with Status 400 Bad Request, for example:
```
{
    "errorMessage": "Validation failed. 1 error(s)",
    "errors": [
        "Invalid url format!!"
    ]
}
```
[Swagger](https://swagger.io/) and [Spring Fox](https://springfox.github.io/springfox/docs/current/) is used as the api documentation framework.
Using ```ExposedApi``` annotation API endpoint can be enabled.

##Assumptions
* The Application does not consider already visited urls for the given request to avoid infinite loops.
* For Child pages it ignores any connection or invalid urls errors, the url node is defaulted to null for such cases.



Author: Ranjeeth Padinhare Thattariyil  
Date:	20 August 2017