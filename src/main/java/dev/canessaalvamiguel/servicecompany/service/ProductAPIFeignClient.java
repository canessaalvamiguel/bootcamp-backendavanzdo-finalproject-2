package dev.canessaalvamiguel.servicecompany.service;

import dev.canessaalvamiguel.servicecompany.configuration.ApiProperties;
import dev.canessaalvamiguel.servicecompany.entities.Product;
import dev.canessaalvamiguel.servicecompany.rest.ProductRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class ProductAPIFeignClient implements IProductAPI{

  private final ProductRestClient productRestClient;
  private String token = "";
  private final ApiProperties apiProperties;

  public ProductAPIFeignClient(ProductRestClient productRestClient, ApiProperties apiProperties) {
    this.productRestClient = productRestClient;
    this.apiProperties = apiProperties;
  }

  @Override
  public void authenticate() {
    token = productRestClient
        .authenticate(
            "Basic " +
                Base64.getEncoder().encodeToString(apiProperties.getCredentials())
        );
  }

  @Override
  public List<Product> getProductByCompanyId(Long companyId) {
    if(token.isEmpty())
      authenticate();

    try {
      return productRestClient.getProductByCompanyId(companyId, "Bearer " + token);
    }catch (ResponseStatusException e) {
      if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
        log.info("UNAUTHORIZED error detected. "+e.getMessage() + " Trying to authenticate again.");
        token = "";
        return getProductByCompanyId(companyId);
      }
      throw e;
    }

  }
}
