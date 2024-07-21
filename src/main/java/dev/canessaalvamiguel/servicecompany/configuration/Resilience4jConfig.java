package dev.canessaalvamiguel.servicecompany.configuration;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.event.RetryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@Configuration
@Slf4j
public class Resilience4jConfig {

  @Bean
  public RetryRegistry retryRegistry() {
    return RetryRegistry.ofDefaults();
  }

  @Bean
  public Retry productClientRetry(RetryRegistry retryRegistry) {
    RetryConfig config = RetryConfig.custom()
        .maxAttempts(3)
        .waitDuration(Duration.ofMillis(500))
        .retryOnException(throwable -> {
          if (throwable instanceof ResponseStatusException) {
            return ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.UNAUTHORIZED;
          }
          return false;
        })
        .build();
    Retry retry = retryRegistry.retry("ProductAPIFeignClient", config);
    retry.getEventPublisher().onRetry(this::logRetry);
    return retry;
  }

  @Bean
  public Retry authenticateRetry(RetryRegistry retryRegistry) {
    RetryConfig config = RetryConfig.custom()
        .maxAttempts(3)
        .waitDuration(Duration.ofMillis(500))
        .retryOnException(throwable -> throwable instanceof RuntimeException)
        .build();
    Retry retry = retryRegistry.retry("authenticate", config);
    retry.getEventPublisher().onRetry(this::logRetry);
    return retry;
  }

  private void logRetry(RetryEvent event) {
    log.info("Retry attempt {} for {}", event.getNumberOfRetryAttempts(), event.getName());
  }
}
