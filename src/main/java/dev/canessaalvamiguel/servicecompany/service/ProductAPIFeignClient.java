package dev.canessaalvamiguel.servicecompany.service;

import dev.canessaalvamiguel.servicecompany.configuration.ApiProperties;
import dev.canessaalvamiguel.servicecompany.entities.Product;
import dev.canessaalvamiguel.servicecompany.rest.ProductRestClient;
import io.github.resilience4j.retry.Retry;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.function.Supplier;

@Service
@Slf4j
public class ProductAPIFeignClient implements IProductAPI {

  private final ProductRestClient productRestClient;
  private String token = "";
  private final ApiProperties apiProperties;
  private final Retry productClientRetry;
  private final Retry authenticateRetry;
  private long tokenExpiryTime = 0;
  private final int TOKEN_VALID_MINUTES = 60;

  public ProductAPIFeignClient(
      ProductRestClient productRestClient,
      ApiProperties apiProperties,
      Retry productClientRetry,
      Retry authenticateRetry
  ) {
    this.productRestClient = productRestClient;
    this.apiProperties = apiProperties;
    this.productClientRetry = productClientRetry;
    this.authenticateRetry = authenticateRetry;
  }

  private boolean isTokenExpired() {
    return System.currentTimeMillis() >= tokenExpiryTime;
  }

  @Override
  public void authenticate() {
    log.info("Authenticating");
    String basicAuth = "Basic " + Base64.getEncoder().encodeToString(apiProperties.getCredentials());
    Supplier<String> supplier = () -> productRestClient.authenticate(basicAuth);

    this.token = Try.ofSupplier(Retry.decorateSupplier(authenticateRetry, supplier))
        .recover(this::handleRetryFallbackForAuthenticate)
        .get();
    this.tokenExpiryTime = System.currentTimeMillis() + (TOKEN_VALID_MINUTES * 60 * 1000);
  }

  private String handleRetryFallbackForAuthenticate(Throwable throwable) {
    log.error("handleRetryFallbackForAuthenticate method called");
    throw new RuntimeException("Unable to authenticate after retries", throwable);
  }

  @Override
  public List<Product> getProductByCompanyId(Long companyId) {
    log.info("Calling Product API");
    if (token.isEmpty() || isTokenExpired()) {
      authenticate();
    }

    Supplier<List<Product>> supplier = () -> productRestClient.getProductByCompanyId(companyId, "Bearer " + token);

    return Try.ofSupplier(Retry.decorateSupplier(productClientRetry, supplier))
        .recover(throwable -> handleRetryFallback(companyId, throwable))
        .get();
  }

  private List<Product> handleRetryFallback(Long companyId, Throwable throwable) {
    log.error("handleRetryFallback method called, companyId: {}", companyId);
    throw new RuntimeException("Unable to fetch product details after retries", throwable);
  }
}