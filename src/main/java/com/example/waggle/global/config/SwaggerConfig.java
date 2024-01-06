package com.example.waggle.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
//    @Bean
//    public OpenAPI openAPI() {
//        Info info = new Info()
//                .title("Waggle API Document")
//                .version("v0.0.1")
//                .description("Waggle의 API 명세서입니다.");
//        return new OpenAPI()
//                .components(new Components())
//                .info(info);
//    }

    @Bean
    public OpenAPI api() {
        Info info = new Info()
                .version("v1.0.0")
                .title("API 타이틀")
                .description("API Description");

        // SecuritySecheme명
        String jwtSchemeName = "access_token";
        // API 요청헤더에 인증정보 포함
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        // SecuritySchemes 등록
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.APIKEY) // HTTP 방식
//                        .scheme("bearer")
                        .in(SecurityScheme.In.COOKIE)
                        .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional)


        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);

    }


}
