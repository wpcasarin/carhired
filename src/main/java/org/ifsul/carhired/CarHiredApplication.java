package org.ifsul.carhired;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Car Rental API", version = "1.0", description = "Basic docs"))
public class CarHiredApplication {

  public static void main(String[] args) {
    SpringApplication.run(CarHiredApplication.class, args);
  }

}
