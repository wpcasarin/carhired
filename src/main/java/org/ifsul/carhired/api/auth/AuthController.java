package org.ifsul.carhired.api.auth;

import lombok.RequiredArgsConstructor;
import org.ifsul.carhired.api.auth.dto.AuthCustomerDTO;
import org.ifsul.carhired.api.auth.dto.AuthLoginDTO;
import org.ifsul.carhired.api.auth.dto.AuthRegisterDTO;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

//    private void addSelfLink(@NotNull AuthCustomerDTO entity) {
//        entity.add(linkTo(methodOn(CustomerController.class).getCustomerById(entity.getUser().getId())).withSelfRel());
//    }

    @PostMapping("register")
    public ResponseEntity<AuthCustomerDTO> register(@RequestBody AuthRegisterDTO request) {
        var entity = service.createUser(request);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.getUser().getId())
                .toUri();
        return ResponseEntity.created(location).body(entity);
    }


    @PostMapping("login")
    public ResponseEntity<AuthCustomerDTO> login(@RequestBody AuthLoginDTO request) {
        var entity = service.authenticate(request);
        return ResponseEntity.ok(entity);
    }

}
