package dev.canessaalvamiguel.servicecompany.service;

import dev.canessaalvamiguel.servicecompany.configuration.ApiProperties;
import dev.canessaalvamiguel.servicecompany.entities.Product;
import dev.canessaalvamiguel.servicecompany.rest.ProductRestClient;
import io.github.resilience4j.retry.Retry;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.List;
import java.util.function.Supplier;

@Service
@Slf4j
public class ProductAPIFeignClient implements IProductAPI{

  private final ProductRestClient productRestClient;
  private String token = "";
  private final ApiProperties apiProperties;
  private final Retry retry;
  private long tokenExpiryTime = 0;
  private final int TOKEN_VALID_MINUTES = 60;

  public ProductAPIFeignClient(
      ProductRestClient productRestClient,
      ApiProperties apiProperties,
      Retry retry) {
    this.productRestClient = productRestClient;
    this.apiProperties = apiProperties;
    this.retry = retry;
  }

  private boolean isTokenExpired() {
    return System.currentTimeMillis() >= tokenExpiryTime;
  }

  @Override
  public void authenticate() {
    String basicAuth = "Basic " + Base64.getEncoder().encodeToString(apiProperties.getCredentials());
    this.token = productRestClient.authenticate(basicAuth);
  }

  @Override
  public List<Product> getProductByCompanyId(Long companyId) {
    if (isTokenExpired()) {
      authenticate();
    }

    Supplier<List<Product>> supplier =
        () -> productRestClient.getProductByCompanyId(companyId, "Bearer " + token);

    return Try.ofSupplier(Retry.decorateSupplier(retry, supplier))
        .recover(throwable -> handleRetryFallback(companyId, throwable))
        .get();
  }

  private List<Product> handleRetryFallback(Long companyId, Throwable throwable) {
    if (throwable instanceof ResponseStatusException) {
      ResponseStatusException ex = (ResponseStatusException) throwable;
      if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
        log.info("UNAUTHORIZED error detected. Trying to authenticate again. " + ex.getMessage());
        authenticate();
        return productRestClient.getProductByCompanyId(companyId, "Bearer " + token);
      }
    }
    log.error("Failed to get product by company ID after retries: " + throwable.getMessage());
    throw new RuntimeException("Unable to fetch product details after retries", throwable);
  }
}
