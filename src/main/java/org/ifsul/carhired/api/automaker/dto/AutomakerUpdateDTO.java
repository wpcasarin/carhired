package org.ifsul.carhired.api.automaker.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AutomakerUpdateDTO {
    @Size(min = 3, max = 100)
    private String name;
    @Size(min = 3, max = 100)
    private String country;
}
