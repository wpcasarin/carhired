package org.ifsul.carhired.api.automaker.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class AutomakerDTO extends RepresentationModel<AutomakerDTO> {
    private Long id;
    private String name;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
