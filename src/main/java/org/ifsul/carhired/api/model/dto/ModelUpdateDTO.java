package org.ifsul.carhired.api.model.dto;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ModelUpdateDTO {
    @Size(min = 3, max = 100)
    private String name;
    @PastOrPresent
    private LocalDate releasedAt;
}
