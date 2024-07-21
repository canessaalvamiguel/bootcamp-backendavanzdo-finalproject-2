package dev.canessaalvamiguel.servicecompany.rest;

import dev.canessaalvamiguel.servicecompany.configuration.FeignConfig;
import dev.canessaalvamiguel.servicecompany.entities.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "product-service", url = "${product-service.url}", configuration = FeignConfig.class)
public interface ProductRestClient {

  @PostMapping("/api/v1/auth/token")
  String authenticate(@RequestHeader("Authorization") String authorizationHeader);

  @GetMapping("/api/v1/products/company/{id}")
  Page<Product> getProductByCompanyId(
      @PathVariable("id") Long companyId,
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestHeader("Authorization") String bearerToken
  );
}
