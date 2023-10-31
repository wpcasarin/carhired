package org.ifsul.carhired.api.model;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ifsul.carhired.api.automaker.AutomakerRepository;
import org.ifsul.carhired.api.model.dto.ModelCreateDTO;
import org.ifsul.carhired.api.model.dto.ModelDTO;
import org.ifsul.carhired.api.model.dto.ModelUpdateDTO;
import org.ifsul.carhired.api.vehicle.VehicleRepository;
import org.ifsul.carhired.api.vehicle.VehicleService;
import org.ifsul.carhired.api.vehicle.dto.VehicleDTO;
import org.modelmapper.ModelMapper;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class ModelService {
    private final ModelRepository repository;
    private final AutomakerRepository automakerRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleService vehicleService;
    private final ModelMapper mapper;

    public List<ModelDTO> findAllModels() {
        return repository
                .findAllByDeletedIsFalse()
                .stream()
                .map(entity -> mapper.map(entity, ModelDTO.class))
                .toList();
    }

    public ModelDTO findModelById(Long id) {
        return repository
                .findByIdAndDeletedIsFalse(id)
                .map(entity -> mapper.map(entity, ModelDTO.class))
                .orElseThrow(() -> new OpenApiResourceNotFoundException("model not found"));
    }

    public Long findAutomakerIdByModelId(Long id) {
        var entity = repository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("automaker not found"));
        return entity.getAutomaker().getId();
    }

    public List<VehicleDTO> findAllVehiclesByModelId(Long id) {
        return vehicleRepository
                .findAllByModelIdAndDeletedIsFalse(id)
                .stream()
                .map(entity -> mapper.map(entity, VehicleDTO.class))
                .toList();
    }

    public ModelDTO createModel(@Valid @NonNull ModelCreateDTO request) {
        var entity = Model.builder()
                .releasedAt(request.getReleasedAt())
                .name(request.getName())
                .deleted(false)
                .automaker(automakerRepository
                        .findByIdAndDeletedIsFalse(request.getAutomakerId())
                        .orElseThrow(() -> new OpenApiResourceNotFoundException("automaker not found")))
                .build();
        entity = repository.save(entity);
        return mapper.map(entity, ModelDTO.class);
    }

    public ModelDTO updateModel(Long id, @Valid @NonNull ModelUpdateDTO request) {
        var entity = repository
                .findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("model not found"));
        mapper.map(request, entity);
        entity = repository.save(entity);
        return mapper.map(entity, ModelDTO.class);
    }

    @Transactional
    public void deleteModelById(Long id) {
        var entity = repository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("model not found"));
        entity.getVehicles().forEach(vehicle -> vehicleService.deleteVehicleById(vehicle.getId()));
        repository.deleteByIdSoftly(id);
    }
}
