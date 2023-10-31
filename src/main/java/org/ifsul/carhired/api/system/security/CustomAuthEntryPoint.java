package org.ifsul.carhired.api.system.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

  private final HandlerExceptionResolver handlerExceptionResolver;

  public CustomAuthEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
    this.handlerExceptionResolver = handlerExceptionResolver;
  }

  @Override
  public void commence(HttpServletRequest request, @NonNull HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"Realm\"");
        this.handlerExceptionResolver.resolveException(request, response, null, authException);
  }
}
