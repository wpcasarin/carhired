package org.ifsul.carhired.api.vehicle;

import lombok.RequiredArgsConstructor;
import org.ifsul.carhired.api.base.BaseController;
import org.ifsul.carhired.api.model.ModelController;
import org.ifsul.carhired.api.vehicle.dto.VehicleCreateDTO;
import org.ifsul.carhired.api.vehicle.dto.VehicleDTO;
import org.ifsul.carhired.api.vehicle.dto.VehicleUpdateDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/vehicle")
public class VehicleController extends BaseController<VehicleDTO> {
    private final VehicleService service;

    private void addVehicleLinks(@NotNull VehicleDTO entity) {
        entity.add(linkTo(methodOn(VehicleController.class).getVehicleById(entity.getId())).withSelfRel());
        entity.add(linkTo(methodOn(VehicleController.class).getAllVehicles()).withRel("allVehicles"));
    }

    private void addModelLink(@NotNull VehicleDTO entity) {
        var automakerId = service.findModelIdByVehicleId(entity.getId());
        entity.add(linkTo(methodOn(ModelController.class).getModelById(automakerId)).withRel("model"));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<VehicleDTO>> getAllVehicles() {
        var entities = service.findAllVehicles();
        if (entities.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            entities.forEach(this::addModelLink);
            return ResponseEntity.ok(addGetAllSelfLink(entities, VehicleController.class));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable("id") Long id) {
        var entity = service.findVehicleById(id);
        addVehicleLinks(entity);
        addModelLink(entity);
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<VehicleDTO> createVehicle(@RequestBody VehicleCreateDTO request) {
        var entity = service.createVehicle(request);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.getId())
                .toUri();
        addVehicleLinks(entity);
        addModelLink(entity);
        return ResponseEntity.created(location).body(entity);
    }

    @PutMapping("{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Long id, @RequestBody VehicleUpdateDTO request) {
        var entity = service.updateVehicle(id, request);
        addVehicleLinks(entity);
        addModelLink(entity);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteVehicleById(@PathVariable Long id) {
        service.deleteVehicleById(id);
        return ResponseEntity.noContent().build();
    }
}