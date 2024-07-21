package dev.canessaalvamiguel.servicecompany.service;

import dev.canessaalvamiguel.servicecompany.entities.Employee;
import dev.canessaalvamiguel.servicecompany.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

  private final EmployeeRepository employeeRepository;

  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public Page<Employee> getEmployeesByCompanyId(Long companyId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return employeeRepository.findByCompanyId(companyId, pageable);
  }
}
