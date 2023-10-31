package org.ifsul.carhired.api.user.customer;

import lombok.RequiredArgsConstructor;
import org.ifsul.carhired.api.aws.S3Service;
import org.ifsul.carhired.api.base.BaseController;
import org.ifsul.carhired.api.user.customer.dto.CustomerDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController extends BaseController<CustomerDTO> {
    private final CustomerService service;
    private final S3Service s3Service;

    private void addSelfLinks(@NotNull CustomerDTO entity) {
        service.getUserWithNonNullPictureUriById(entity.getId())
                .ifPresent(user -> {
                    if (user.getPictureUri() != null) {
                        var url = s3Service.getPresidedUrl(user.getPictureUri());
                        Link profilePictureLink = Link.of(url, "profilePicture");
                        entity.add(profilePictureLink);
                    }
                });
        entity.add(linkTo(methodOn(CustomerController.class).getCustomerById(entity.getId())).withSelfRel());
    }

    @GetMapping
    public ResponseEntity<CollectionModel<CustomerDTO>> getAllCustomers() {
        var entities = service.findAllCustomers();
        if (entities.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(addGetAllSelfLink(entities, CustomerController.class));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        var entity = service.findCustomerById(id);
        addSelfLinks(entity);
        return ResponseEntity.ok(entity);
    }

    @PatchMapping("{id}")
    public ResponseEntity<CustomerDTO> addProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        var entity = service.addUserProfilePicture(id, file);
        addSelfLinks(entity);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable Long id) {
        service.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }

}
