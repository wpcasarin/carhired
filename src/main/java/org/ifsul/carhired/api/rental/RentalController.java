package org.ifsul.carhired.api.rental;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.ifsul.carhired.api.base.BaseController;
import org.ifsul.carhired.api.rental.dto.RentalCreateDTO;
import org.ifsul.carhired.api.rental.dto.RentalDTO;
import org.ifsul.carhired.api.rental.dto.RentalReturnDTO;
import org.ifsul.carhired.api.vehicle.dto.VehicleDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/rental")
public class RentalController extends BaseController<RentalDTO> {
    private final RentalService service;

    private CollectionModel<VehicleDTO> addVehiclesLink(@NotNull CollectionModel<VehicleDTO> entities) {
        return entities.add(linkTo(methodOn(RentalController.class).getAllAvailableVehicles()).withSelfRel());
    }

    private void addSelfLink(@NotNull RentalDTO entity) {
        entity.add(linkTo(methodOn(RentalController.class).getRentalById(entity.getId())).withSelfRel());
    }

    private void addAllLinks(@NotNull RentalDTO entity) {
        entity.add(linkTo(methodOn(RentalController.class).getRentalById(entity.getId())).withSelfRel());
        entity.add(linkTo(methodOn(RentalController.class).getAllRentals(null)).withRel("rentals"));
        entity.add(linkTo(methodOn(RentalController.class).getAllRentals(RentalStatus.ACTIVE)).withRel("activeRentals"));
        entity.add(linkTo(methodOn(RentalController.class).getAllRentals(RentalStatus.FINISHED)).withRel("finishedRentals"));
    }

    @GetMapping("/vehicles")
    public ResponseEntity<CollectionModel<VehicleDTO>> getAllAvailableVehicles() {
        var entities = service.findAvailableVehicles();
        if (entities.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(addVehiclesLink(CollectionModel.of(entities)));
        }
    }

    @GetMapping
    public ResponseEntity<CollectionModel<RentalDTO>> getAllRentals(@RequestParam(name = "status", required = false) RentalStatus status) {
        var entities = status != null && EnumUtils.isValidEnum(RentalStatus.class, status.name())
                ? service.findAllRentalsStatus(status)
                : service.findAllRentals();
        if (entities.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            entities.forEach(this::addSelfLink);
            return ResponseEntity.ok(addGetAllSelfLink(entities, RentalController.class));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable("id") Long id) {
        var entity = service.findRentalById(id);
        addAllLinks(entity);
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<RentalDTO> createRental(@NotNull HttpServletRequest request, @RequestBody RentalCreateDTO body) {
        String token = request.getHeader("Authorization").substring(7);
        return ResponseEntity.ok(service.createRental(body, token));
    }

    @PatchMapping("{id}")
    public ResponseEntity<RentalDTO> returnRental(@PathVariable Long id, @RequestBody RentalReturnDTO body) {
        return ResponseEntity.ok(service.returnRental(id, body));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteRentalById(@PathVariable Long id) {
        service.deleteRentalById(id);
        return ResponseEntity.noContent().build();
    }
}