package bit.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("Bit-o-api")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenApi() {
        // JWT 방식의 Security Scheme 정의
        final String securitySchemeName = "액세스 토큰을 입력하세요.";

        return new OpenAPI()
                .info(new Info()
                        .title("BitO 스터디 API")
                        .version("v1")
                        .description("API 명세서 입니다.")
                )
//                .addServersItem(new Server().url("http://www.localhost:8080"))
//                .addServersItem(new Server().url("https://www.bit-o.shop"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components().addSecuritySchemes(securitySchemeName,
                        new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer")
                                .bearerFormat("JWT"))); // JWT 설정
    }
}
