package dev.canessaalvamiguel.servicecompany.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomErrorDecoder implements ErrorDecoder {

  private final ErrorDecoder defaultErrorDecoder = new Default();

  @Override
  public Exception decode(String methodKey, Response response) {
    if (response.status() == HttpStatus.UNAUTHORIZED.value()) {
      return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized: Invalid credentials or token expired.");
    }
    return defaultErrorDecoder.decode(methodKey, response);
  }
}
