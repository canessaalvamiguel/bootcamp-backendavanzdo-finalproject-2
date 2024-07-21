package dev.canessaalvamiguel.servicecompany.service;


import dev.canessaalvamiguel.servicecompany.entities.Product;

import java.util.List;

public interface IProductAPI {
  void authenticate();
  List<Product> getProductByCompanyId( Long companyId);
}
