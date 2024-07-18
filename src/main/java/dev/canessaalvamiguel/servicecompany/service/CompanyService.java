package dev.canessaalvamiguel.servicecompany.service;

import dev.canessaalvamiguel.servicecompany.entities.Company;
import dev.canessaalvamiguel.servicecompany.exceptions.RucExistsException;
import dev.canessaalvamiguel.servicecompany.repository.CompanyRepository;
import dev.canessaalvamiguel.servicecompany.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {

  CompanyRepository companyRepository;

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
}
