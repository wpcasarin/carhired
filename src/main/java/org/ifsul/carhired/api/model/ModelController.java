package org.ifsul.carhired.api.model;

import lombok.RequiredArgsConstructor;
import org.ifsul.carhired.api.automaker.AutomakerController;
import org.ifsul.carhired.api.base.BaseController;
import org.ifsul.carhired.api.model.dto.ModelCreateDTO;
import org.ifsul.carhired.api.model.dto.ModelDTO;
import org.ifsul.carhired.api.model.dto.ModelUpdateDTO;
import org.ifsul.carhired.api.vehicle.VehicleController;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/model")
public class ModelController extends BaseController<ModelDTO> {
    private final ModelService service;

    private void addModelLinks(@NotNull ModelDTO entity) {
        entity.add(linkTo(methodOn(ModelController.class).getModelById(entity.getId())).withSelfRel());
        entity.add(linkTo(methodOn(ModelController.class).getAllModels()).withRel("models"));
    }

    private void addAutomakerLink(@NotNull ModelDTO entity) {
        var automakerId = service.findAutomakerIdByModelId(entity.getId());
        entity.add(linkTo(methodOn(AutomakerController.class).getAutomakerById(automakerId)).withRel("automaker"));
    }

    private void addVehiclesLink(@NotNull ModelDTO entity) {
        var models = service.findAllVehiclesByModelId(entity.getId());
        models.forEach(vehicle -> {
            entity.add(linkTo(methodOn(VehicleController.class).getVehicleById(vehicle.getId())).withRel("vehicles"));
        });
    }

    @GetMapping
    public ResponseEntity<CollectionModel<ModelDTO>> getAllModels() {
        var entities = service.findAllModels();
        if (entities.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            entities.forEach(this::addAutomakerLink);
            return ResponseEntity.ok(addGetAllSelfLink(entities, ModelController.class));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ModelDTO> getModelById(@PathVariable("id") Long id) {
        var entity = service.findModelById(id);
        addModelLinks(entity);
        addAutomakerLink(entity);
        addVehiclesLink(entity);
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<ModelDTO> createModel(@RequestBody ModelCreateDTO request) {
        var entity = service.createModel(request);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.getId())
                .toUri();
        addModelLinks(entity);
        addAutomakerLink(entity);
        return ResponseEntity.created(location).body(entity);
    }

    @PutMapping("{id}")
    public ResponseEntity<ModelDTO> updateModel(@PathVariable Long id, @RequestBody ModelUpdateDTO request) {
        var entity = service.updateModel(id, request);
        addModelLinks(entity);
        addAutomakerLink(entity);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteModelById(@PathVariable Long id) {
        service.deleteModelById(id);
        return ResponseEntity.noContent().build();
    }
}