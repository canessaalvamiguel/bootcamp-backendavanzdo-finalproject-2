package dev.canessaalvamiguel.serviceuser.exceptions;

public class EmailExistsException extends RuntimeException {

  public EmailExistsException(String message) {
    super(message);
  }
}
