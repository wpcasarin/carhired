package org.ifsul.carhired.api.system.exception;

import io.jsonwebtoken.JwtException;

public class InvalidBearerTokenException extends JwtException {
  public InvalidBearerTokenException(String message) {
    super(message);
  }
}
