package org.ifsul.carhired.api.vehicle.dto;

import jakarta.validation.constraints.*;
import lombok.Data;


import java.math.BigDecimal;

@Data
public class VehicleCreateDTO {
    @NotNull
    private Long modelId;
    @NotNull
    @Pattern(regexp = "^[A-Z0-9]{1,7}$", message = "invalid license plaque (VRM)")
    private String licensePlaque;
    @NotNull
    @Size(min = 3, max = 50)
    private String color;
    @Positive
    @Max(50)
    private Byte numDoors;
    @NotNull
    @PositiveOrZero
    @Max(999999)
    private Integer mileage;
    @NotNull
    @Pattern(regexp = "[0-9]{11}", message = "invalid renavam")
    private String renavam;
    @NotNull
    @Pattern(regexp = "\\b[A-HJ-NPR-Z0-9]{17}\\b", message = "invalid chassis (VIN)")
    private String chassis;
    @NotNull
    @DecimalMin(value = "0.01")
    @Digits(integer = 15, fraction = 2)
    private BigDecimal rentPrice;
}
