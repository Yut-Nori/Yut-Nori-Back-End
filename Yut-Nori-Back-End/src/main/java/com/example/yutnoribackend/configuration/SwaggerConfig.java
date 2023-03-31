package com.example.yutnoribackend.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi
                .builder()
                .group("v1-definition") //group 이름 설정
                .pathsToMatch("/api/**") // 적용할 api주소
                .build();
    }

    // swagger 화면에서 표시 정보
    @Bean
    public OpenAPI springShopOpenAPI(){
        return new OpenAPI()
                .info(new Info().title("Yut-Nori API")
                        .description("Yut-Nori 프로젝트 API 명세")
                        .version("v0.0.1"));

    }
}
