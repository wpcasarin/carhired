package org.ifsul.carhired.api.rental.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RentalCreateDTO {
    @NotNull
    @Min(1)
    @Positive
    private Integer period;
    @NotNull
    private Long vehicleId;
}
