package org.ifsul.carhired.api.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ifsul.carhired.api.auth.dto.AuthCustomerDTO;
import org.ifsul.carhired.api.auth.dto.AuthLoginDTO;
import org.ifsul.carhired.api.auth.dto.AuthRegisterDTO;
import org.ifsul.carhired.api.system.config.JsonSerializer;
import org.ifsul.carhired.api.system.exception.EmailAlreadyExistsException;
import org.ifsul.carhired.api.system.security.JwtService;
import org.ifsul.carhired.api.user.Role;
import org.ifsul.carhired.api.user.UserRepository;
import org.ifsul.carhired.api.user.customer.Customer;
import org.ifsul.carhired.api.user.customer.dto.CustomerAuthDTO;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

import static org.ifsul.carhired.api.system.config.CacheRecords.CACHE_ALL_CUSTOMERS;
import static org.ifsul.carhired.api.system.config.CacheRecords.CACHE_AUTH_CUSTOMER;

@Service
@Validated
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ModelMapper mapper;
    private final ValueOperations<String, String> cacheOps;
    private final JsonSerializer<AuthCustomerDTO> authCustomerSerializer;

    public AuthCustomerDTO createUser(@Valid @NonNull AuthRegisterDTO request) {
        if (userRepository.existsUserByEmailAndDeletedIsFalse(request.getEmail()))
            throw new EmailAlreadyExistsException("email: already in use");

        var entity = Customer.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .cpf(request.getCpf())
                .role(Role.CUSTOMER)
                .deleted(false)
                .build();
        entity = userRepository.save(entity);
        var token = jwtService.generateToken(entity);
        cacheOps.getAndDelete(CACHE_ALL_CUSTOMERS);
        return AuthCustomerDTO.builder()
                .user(mapper.map(entity, CustomerAuthDTO.class))
                .token(token)
                .build();
    }


    public AuthCustomerDTO authenticate(@Valid @NonNull AuthLoginDTO request) {
        if (!userRepository.existsUserByEmailAndDeletedIsFalse(request.getEmail()))
            throw new UsernameNotFoundException("bad credentials");
        var typeReference = new TypeReference<AuthCustomerDTO>() {
        };
        final String cacheKey = CACHE_AUTH_CUSTOMER + ":" + request.getEmail();
        var entityString = cacheOps.get(cacheKey);

        if (entityString == null) {
            var user = userRepository
                    .findUserByEmailAndDeletedIsFalse(request.getEmail())
                    .orElseThrow();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            var token = jwtService.generateToken(user);
            var entity = AuthCustomerDTO.builder()
                    .user(mapper.map(user, CustomerAuthDTO.class))
                    .token(token)
                    .build();
            entityString = authCustomerSerializer.encode(entity);
            cacheOps.set(cacheKey, entityString, Duration.ofHours(1));
            return entity;
        }
        return authCustomerSerializer.decode(entityString, typeReference);
    }

}
