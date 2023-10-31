package org.ifsul.carhired.api.automaker;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ifsul.carhired.api.automaker.dto.AutomakerCreateDTO;
import org.ifsul.carhired.api.automaker.dto.AutomakerDTO;
import org.ifsul.carhired.api.automaker.dto.AutomakerUpdateDTO;
import org.ifsul.carhired.api.model.ModelRepository;
import org.ifsul.carhired.api.model.ModelService;
import org.ifsul.carhired.api.model.dto.ModelDTO;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class AutomakerService {
    private final AutomakerRepository repository;
    private final ModelRepository modelRepository;
    private final ModelService modelService;
    private final ModelMapper mapper;

    public List<AutomakerDTO> findAllAutomakers() {
        return repository
                .findAllByDeletedIsFalse()
                .stream()
                .map(entity -> mapper.map(entity, AutomakerDTO.class))
                .toList();
    }

    public AutomakerDTO findAutomakerById(Long id) {
        return repository
                .findByIdAndDeletedIsFalse(id)
                .map(entity -> mapper.map(entity, AutomakerDTO.class))
                .orElseThrow(() -> new OpenApiResourceNotFoundException("automaker not found"));
    }

    public List<ModelDTO> findAllModelsByAutomakerId(Long id) {
        return modelRepository
                .findAllByAutomakerIdAndDeletedIsFalse(id)
                .stream()
                .map(entity -> mapper.map(entity, ModelDTO.class))
                .toList();
    }

    public AutomakerDTO createAutomaker(@Valid @NotNull AutomakerCreateDTO request) {
        var entity = Automaker
                .builder()
                .name(request.getName())
                .country(request.getCountry())
                .deleted(false)
                .build();
        entity = repository.save(entity);
        return mapper.map(entity, AutomakerDTO.class);
    }

    public AutomakerDTO updateAutomaker(Long id, @Valid @NonNull AutomakerUpdateDTO request) {
        var entity = repository
                .findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("automaker not found"));
        mapper.map(request, entity);
        entity = repository.save(entity);
        return mapper.map(entity, AutomakerDTO.class);
    }

    @Transactional
    public void deleteAutomakerById(Long id) {
        var entity = repository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("automaker not found"));
        entity.getModels().forEach(model -> modelService.deleteModelById(model.getId()));
        repository.deleteByIdSoftly(id);
    }
}
