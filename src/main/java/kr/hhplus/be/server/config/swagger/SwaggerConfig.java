package kr.hhplus.be.server.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("HHPlus E-Commerce API")
                        .version("1.0.0")
                        .description("API documentation for HHPlus E-Commerce system"));
    }
}
