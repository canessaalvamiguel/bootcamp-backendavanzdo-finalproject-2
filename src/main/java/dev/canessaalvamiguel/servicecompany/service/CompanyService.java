package dev.canessaalvamiguel.servicecompany.service;

import dev.canessaalvamiguel.servicecompany.entities.Company;
import dev.canessaalvamiguel.servicecompany.entities.Product;
import dev.canessaalvamiguel.servicecompany.exceptions.RucExistsException;
import dev.canessaalvamiguel.servicecompany.repository.CompanyRepository;
import dev.canessaalvamiguel.servicecompany.exceptions.NotFoundException;
import dev.canessaalvamiguel.servicecompany.rest.ProductRestClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {

  CompanyRepository companyRepository;
  ProductRestClient productRestClient;

  public List<Company> getCompanies(){
    return companyRepository.findAll();
  }

  public Company getCompanyById(Long companyId){
    return companyRepository.findById(companyId)
        .orElseThrow(
            () -> new NotFoundException("Company with id " + companyId + " not found.")
        );
  }

  public Company createCompany(Company company){
    if (companyRepository.findByRuc(company.getRuc()) != null) {
      throw new RucExistsException("Ruc already exists");
    }
    return companyRepository.save(company);
  }

  public List<Product> getProductsByCompany(Long companyId){
    String token = productRestClient
        .authenticate(
            "Basic " +
            Base64.getEncoder().encodeToString("user:password".getBytes())
        );

    return productRestClient.getProductByCompanyId(companyId, "Bearer " + token);
  }
}
