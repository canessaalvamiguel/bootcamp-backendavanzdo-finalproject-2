package dev.canessaalvamiguel.servicecompany;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServiceCompanyApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServiceCompanyApplication.class, args);
  }

}
