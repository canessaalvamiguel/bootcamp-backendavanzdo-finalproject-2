package dev.canessaalvamiguel.servicecompany.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiProperties {

  @Value("${product.api.username}")
  private String username;

  @Value("${product.api.password}")
  private String password;

  public byte[] getCredentials(){
    return (username + ":" + password).getBytes();
  }
}
