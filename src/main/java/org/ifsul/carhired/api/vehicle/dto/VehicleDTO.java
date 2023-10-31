package org.ifsul.carhired.api.vehicle.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleDTO extends RepresentationModel<VehicleDTO> {
    private Long id;
    private String licensePlaque;
    private String color;
    private Byte numDoors;
    private Integer mileage;
    private String renavam;
    private String chassis;
    private BigDecimal rentPrice;
    private Boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
