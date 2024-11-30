package QCraft.QCraft.security.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@Configuration
public class ClaudeApiConfig {

    //claude api 템플릿 기본설정
    @Bean
    public RestTemplate claudeRestTemplate(RestTemplateBuilder builder, @Value("${claude.api.key}")String apiKey) {
        return builder
                .defaultHeader("x-api-key", apiKey)
                .defaultHeader("anthropic-version", "2023-06-01")
                .defaultHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }
}
