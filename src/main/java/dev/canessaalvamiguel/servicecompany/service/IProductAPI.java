package dev.canessaalvamiguel.servicecompany.service;


import dev.canessaalvamiguel.servicecompany.entities.Product;
import org.springframework.data.domain.Page;

public interface IProductAPI {
  void authenticate();
  Page<Product> getProductByCompanyId(Long companyId, int page, int size);
}
