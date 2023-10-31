package org.ifsul.carhired.api.rental.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class RentalReturnDTO {
    @NotNull
    @PositiveOrZero
    @Max(999999)
    private Integer mileage;
}
