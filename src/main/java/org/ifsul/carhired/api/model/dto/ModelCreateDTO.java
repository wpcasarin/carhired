package org.ifsul.carhired.api.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ModelCreateDTO {
    @NotNull
    @Positive
    private Long automakerId;
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;
    @PastOrPresent
    private LocalDate releasedAt;
}