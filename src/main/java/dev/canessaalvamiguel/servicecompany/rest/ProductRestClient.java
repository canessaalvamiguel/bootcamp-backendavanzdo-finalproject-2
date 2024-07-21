package dev.canessaalvamiguel.servicecompany.rest;

import dev.canessaalvamiguel.servicecompany.entities.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;


@FeignClient(name = "product-service", url = "http://localhost:8081")
public interface ProductRestClient {

  @PostMapping("/api/v1/auth/token")
  String authenticate(@RequestHeader("Authorization") String authorizationHeader);

  @GetMapping("/api/v1/products/company/{id}")
  List<Product> getProductByCompanyId(
      @PathVariable("id") Long companyId,
      @RequestHeader("Authorization") String bearerToken
  );
}
