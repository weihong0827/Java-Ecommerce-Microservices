package tech.qiuweihong.config;


import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

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
                .build();
    }
    private ApiInfo getAPIInfo(){
        return new ApiInfoBuilder()
                .title("Ecommerce Platform API Documentation")
                .description("This is a documentation for Ecommerce Platform API Documentation")
                .contact(new Contact("Qiu Weihong","",""))
                .version("1.0")
                .build();
    }

}
