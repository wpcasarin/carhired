package org.ifsul.carhired.api.rental;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ifsul.carhired.api.rental.dto.RentalDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
@RequiredArgsConstructor
public class RentalMapper {
    private final ModelMapper modelMapper;

    public RentalDTO map(@NonNull Rental entity) {
        var mappedEntity = modelMapper.map(entity, RentalDTO.class);
        var rentPrice = mappedEntity.getVehicle().getRentPrice();
        var start = mappedEntity.getCreatedAt().toLocalDate();
        var end = mappedEntity.getDueDate();
        var days = DAYS.between(start, end);
        var total = rentPrice.multiply(BigDecimal.valueOf(days));
        mappedEntity.setTotalFee(total);
        return mappedEntity;
    }
}
