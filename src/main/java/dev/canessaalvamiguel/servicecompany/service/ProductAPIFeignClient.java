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
  private final Retry retry;
  private long tokenExpiryTime = 0;
  private final int TOKEN_VALID_MINUTES = 60;

  public ProductAPIFeignClient(
      ProductRestClient productRestClient,
      ApiProperties apiProperties,
      Retry retry
  ) {
    this.productRestClient = productRestClient;
    this.apiProperties = apiProperties;
    this.retry = retry;
  }

  private boolean isTokenExpired() {
    return System.currentTimeMillis() >= tokenExpiryTime;
  }

  @Override
  public void authenticate() {
    log.info("Authenticating");
    String basicAuth = "Basic " + Base64.getEncoder().encodeToString(apiProperties.getCredentials());
    this.token = productRestClient.authenticate(basicAuth);
    this.tokenExpiryTime = System.currentTimeMillis() + (TOKEN_VALID_MINUTES * 60 * 1000);
  }

  @Override
  public List<Product> getProductByCompanyId(Long companyId) {
    log.info("Calling Product API");
    if (token.isEmpty() || isTokenExpired()) {
      authenticate();
    }

    Supplier<List<Product>> supplier = () -> productRestClient.getProductByCompanyId(companyId, "Bearer " + token);

    return Try.ofSupplier(Retry.decorateSupplier(retry, supplier))
        .recover(throwable -> handleRetryFallback(companyId, throwable))
        .get();
  }

  private List<Product> handleRetryFallback(Long companyId, Throwable throwable) {
    log.info("handleRetryFallback method called");
    throw new RuntimeException("Unable to fetch product details after retries", throwable);
  }
}