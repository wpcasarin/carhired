package org.ifsul.carhired.api.vehicle.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleUpdateDTO {
    @Pattern(regexp = "^[A-Z0-9]{1,7}$", message = "invalid license plaque (VRM)")
    private String licensePlaque;
    @NotNull
    @Size(min = 3, max = 50)
    private String color;
    @PositiveOrZero
    private Integer mileage;
    @DecimalMin(value = "0.01")
    @Digits(integer = 15, fraction = 2)
    private BigDecimal rentPrice;
}
