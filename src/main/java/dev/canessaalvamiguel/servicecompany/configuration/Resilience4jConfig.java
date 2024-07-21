package dev.canessaalvamiguel.servicecompany.configuration;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@Configuration
public class Resilience4jConfig {

  @Bean
  public RetryRegistry retryRegistry() {
    return RetryRegistry.ofDefaults();
  }

  @Bean
  public Retry retry(RetryRegistry retryRegistry) {
    return retryRegistry.retry("productClient", RetryConfig.custom()
        .maxAttempts(3)
        .waitDuration(Duration.ofMillis(500))
        .retryExceptions(ResponseStatusException.class)
        .build());
  }
}
