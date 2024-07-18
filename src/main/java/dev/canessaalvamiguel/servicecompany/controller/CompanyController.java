package dev.canessaalvamiguel.servicecompany.controller;

import dev.canessaalvamiguel.servicecompany.entities.Company;
import dev.canessaalvamiguel.servicecompany.service.CompanyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@Slf4j
@AllArgsConstructor
public class CompanyController {

  CompanyService companyService;
  //TODO: Implement pagination
  @GetMapping
  public ResponseEntity<List<Company>> getCompanies(){
    log.info("Getting all companies");
    return ResponseEntity.ok(companyService.getCompanies());
  }

  @GetMapping("/{companyId}")
  public ResponseEntity<Company> getCompanyById(@PathVariable Long companyId){
    log.info("Getting company by id: {}", companyId);
    return ResponseEntity.ok(companyService.getCompanyById(companyId));
  }

  @PostMapping
  public ResponseEntity<Company> createCompany(@RequestBody Company company){
    log.info("Creating new company: {}", company);
    return ResponseEntity.ok(companyService.createCompany(company));
  }

}
