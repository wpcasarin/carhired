package org.ifsul.carhired.api.automaker;

import lombok.RequiredArgsConstructor;
import org.ifsul.carhired.api.automaker.dto.AutomakerCreateDTO;
import org.ifsul.carhired.api.automaker.dto.AutomakerDTO;
import org.ifsul.carhired.api.automaker.dto.AutomakerUpdateDTO;
import org.ifsul.carhired.api.model.ModelController;
import org.ifsul.carhired.api.base.BaseController;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/automaker")
public class AutomakerController extends BaseController<AutomakerDTO> {
    private final AutomakerService service;

    private void addAutomakerSelfLink(@NotNull AutomakerDTO entity) {
        entity.add(linkTo(methodOn(AutomakerController.class).getAutomakerById(entity.getId())).withSelfRel());
    }

    private void addAutomakerLinks(@NotNull AutomakerDTO entity) {
        addAutomakerSelfLink(entity);
        entity.add(linkTo(methodOn(AutomakerController.class).getAllAutomakers()).withRel("automakers"));
    }

    private void addModelsLink(@NotNull AutomakerDTO entity) {
        var models = service.findAllModelsByAutomakerId(entity.getId());
        models.forEach(model -> {
            entity.add(linkTo(methodOn(ModelController.class).getModelById(model.getId())).withRel("models"));
        });
    }

    @GetMapping
    public ResponseEntity<CollectionModel<AutomakerDTO>> getAllAutomakers() {
        var entities = service.findAllAutomakers();
        if (entities.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            entities.forEach(this::addAutomakerSelfLink);
            return ResponseEntity.ok(addGetAllSelfLink(entities, AutomakerController.class));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<AutomakerDTO> getAutomakerById(@PathVariable("id") Long id) {
        var entity = service.findAutomakerById(id);
        addAutomakerLinks(entity);
        addModelsLink(entity);
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<AutomakerDTO> createAutomaker(@RequestBody AutomakerCreateDTO request) {
        var entity = service.createAutomaker(request);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.getId())
                .toUri();
        addAutomakerLinks(entity);
        return ResponseEntity.created(location).body(entity);
    }

    @PutMapping("{id}")
    public ResponseEntity<AutomakerDTO> updateAutomaker(@PathVariable Long id, @RequestBody AutomakerUpdateDTO request) {
        var entity = service.updateAutomaker(id, request);
        addAutomakerLinks(entity);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAutomakerById(@PathVariable Long id) {
        service.deleteAutomakerById(id);
        return ResponseEntity.noContent().build();
    }
}
