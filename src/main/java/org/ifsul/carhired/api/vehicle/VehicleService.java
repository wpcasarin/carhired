package org.ifsul.carhired.api.vehicle;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ifsul.carhired.api.model.ModelRepository;
import org.ifsul.carhired.api.rental.RentalService;
import org.ifsul.carhired.api.system.exception.BadRequestException;
import org.ifsul.carhired.api.vehicle.dto.VehicleCreateDTO;
import org.ifsul.carhired.api.vehicle.dto.VehicleDTO;
import org.ifsul.carhired.api.vehicle.dto.VehicleUpdateDTO;
import org.modelmapper.ModelMapper;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository repository;
    private final ModelRepository modelRepository;
    private final RentalService rentalService;
    private final ModelMapper mapper;

    public List<VehicleDTO> findAllVehicles() {
        return repository
                .findAllByDeletedIsFalse()
                .stream()
                .map(entity -> mapper.map(entity, VehicleDTO.class))
                .toList();
    }

    public VehicleDTO findVehicleById(Long id) {
        return repository
                .findByIdAndDeletedIsFalse(id)
                .map(entity -> mapper.map(entity, VehicleDTO.class))
                .orElseThrow(() -> new OpenApiResourceNotFoundException("vehicle not found"));
    }

    public Long findModelIdByVehicleId(Long id) {
        var entity = repository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("model not found"));
        return entity.getModel().getId();
    }

    public VehicleDTO createVehicle(@Valid @NonNull VehicleCreateDTO request) {
        var entity = Vehicle.builder()
                .licensePlaque(request.getLicensePlaque())
                .color(request.getColor().toUpperCase())
                .mileage(request.getMileage())
                .numDoors(request.getNumDoors())
                .renavam(request.getRenavam().toUpperCase())
                .rentPrice(request.getRentPrice())
                .chassis(request.getChassis().toUpperCase())
                .deleted(false)
                .available(true)
                .model(modelRepository
                        .findByIdAndDeletedIsFalse(request.getModelId())
                        .orElseThrow(() -> new OpenApiResourceNotFoundException("model not found")))
                .build();
        entity = repository.save(entity);
        return mapper.map(entity, VehicleDTO.class);
    }

    public VehicleDTO updateVehicle(Long id, @Valid @NonNull VehicleUpdateDTO request) {
        var entity = repository
                .findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("vehicle not found"));
        mapper.map(request, entity);
        entity = repository.save(entity);
        return mapper.map(entity, VehicleDTO.class);
    }

    @Transactional
    public void deleteVehicleById(Long id) {
        var entity = repository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("vehicle not found"));
        if (entity.getAvailable()) {
            entity.getRentals().forEach(rental -> rentalService.deleteRentalById(rental.getId()));
            repository.deleteByIdSoftly(id);
        } else {
            throw new BadRequestException("vehicle has an active lease");
        }
    }
}
