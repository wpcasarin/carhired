package org.ifsul.carhired.api.vehicle.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class VehicleRentalDTO {
    private String licensePlaque;
    private String color;
    private Byte numDoors;
    private Integer mileage;
    private String renavam;
    private String chassis;
    private BigDecimal rentPrice;
}
