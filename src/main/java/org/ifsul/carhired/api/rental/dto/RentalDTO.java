package org.ifsul.carhired.api.rental.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ifsul.carhired.api.user.customer.dto.CustomerRentalDTO;
import org.ifsul.carhired.api.vehicle.dto.VehicleRentalDTO;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class RentalDTO extends RepresentationModel<RentalDTO> {
    private Long id;
    private BigDecimal totalFee;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnedAt;
    private VehicleRentalDTO vehicle;
    private CustomerRentalDTO customer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
