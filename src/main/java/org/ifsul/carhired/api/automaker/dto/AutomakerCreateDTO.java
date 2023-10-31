package org.ifsul.carhired.api.automaker.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AutomakerCreateDTO {
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;
    @Size(min = 3, max = 100)
    private String country;
}
