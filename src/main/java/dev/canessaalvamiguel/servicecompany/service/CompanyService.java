package dev.canessaalvamiguel.servicecompany.service;

import dev.canessaalvamiguel.servicecompany.entities.Company;
import dev.canessaalvamiguel.servicecompany.entities.Product;
import dev.canessaalvamiguel.servicecompany.exceptions.NotFoundException;
import dev.canessaalvamiguel.servicecompany.exceptions.RucExistsException;
import dev.canessaalvamiguel.servicecompany.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final IProductAPI productRestClient;

  public Page<Company> getCompanies(int page, int size){
    Pageable pageable = PageRequest.of(page, size);
    return companyRepository.findAll(pageable);
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

  public Page<Product> getProductsByCompany(Long companyId, int page, int size){
    return productRestClient.getProductByCompanyId(companyId, page, size);
  }
}
