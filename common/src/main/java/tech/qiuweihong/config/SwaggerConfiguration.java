package tech.qiuweihong.config;


import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Data
@EnableOpenApi
public class SwaggerConfiguration {
    @Bean
    public Docket webApiDoc(){
        return new Docket(DocumentationType.OAS_30)
                .groupName("Client API Documentation")
                .pathMapping("/")
                .enable(true)
                .apiInfo(getAPIInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("tech.qiuweihong"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()));

    }
    @Bean
    public Docket adminApiDoc(){
        return new Docket(DocumentationType.OAS_30)
                .groupName("Admin API Documentation")
                .pathMapping("/")
                .enable(true)
                .apiInfo(getAPIInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("tech.qiuweihong"))
                .paths(PathSelectors.ant("/admin/**"))
                .build()
                .pathMapping("/api")
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()));
    }
    private ApiInfo getAPIInfo(){
        return new ApiInfoBuilder()
                .title("Ecommerce Platform API Documentation")
                .description("This is a documentation for Ecommerce Platform API Documentation")
                .contact(new Contact("Qiu Weihong","",""))
                .version("1.0")
                .build();
    }
    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
        return Collections.singletonList(
                new SecurityReference("JWT", authorizationScopes));
    }



}
