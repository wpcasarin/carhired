package org.ifsul.carhired.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
public class ModelDTO extends RepresentationModel<ModelDTO> {
    private Long id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releasedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
