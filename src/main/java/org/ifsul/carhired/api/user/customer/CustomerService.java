package org.ifsul.carhired.api.user.customer;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.ifsul.carhired.api.aws.S3Service;
import org.ifsul.carhired.api.rental.RentalRepository;
import org.ifsul.carhired.api.system.config.JsonSerializer;
import org.ifsul.carhired.api.system.exception.BadRequestException;
import org.ifsul.carhired.api.user.User;
import org.ifsul.carhired.api.user.UserRepository;
import org.ifsul.carhired.api.user.UserService;
import org.ifsul.carhired.api.user.customer.dto.CustomerDTO;
import org.modelmapper.ModelMapper;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.ifsul.carhired.api.system.config.CacheRecords.CACHE_ALL_CUSTOMERS;
import static org.ifsul.carhired.api.system.config.CacheRecords.CACHE_CUSTOMER;
import static org.ifsul.carhired.api.user.Role.ADMIN;
import static org.ifsul.carhired.api.user.Role.CUSTOMER;

@Service
@Validated
@RequiredArgsConstructor
public class CustomerService extends UserService {
    private final UserRepository repository;
    private final RentalRepository rentalRepository;
    private final S3Service s3Service;
    private final ModelMapper mapper;
    private final ValueOperations<String, String> cacheOps;
    private final JsonSerializer<List<CustomerDTO>> customersSerializer;


    public List<CustomerDTO> findAllCustomers() {
        var typeReference = new TypeReference<List<CustomerDTO>>() {
        };
        var customersString = cacheOps.get(CACHE_ALL_CUSTOMERS);
        if (customersString == null) {
            var customers = repository
                    .findAllByRoleEqualsAndDeletedIsFalse(CUSTOMER)
                    .stream()
                    .map(user -> mapper.map(user, CustomerDTO.class))
                    .toList();
            customersString = customersSerializer.encode(customers);
            cacheOps.set(CACHE_ALL_CUSTOMERS, customersString, Duration.ofMinutes(60));
            return customers;
        }
        return customersSerializer.decode(customersString, typeReference);
    }

    public CustomerDTO findCustomerById(Long id) {
        var entity = repository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("customer not found"));
        checkAccessForUser(entity);
        return mapper.map(entity, CustomerDTO.class);
    }

    public CustomerDTO addUserProfilePicture(Long id, MultipartFile file) {
        var entity = repository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("customer not found"));
        checkAccessForUser(entity);
        if (entity.getPictureUri() != null) {
            throw new BadRequestException("user already have a picture");
        }
        try {
            var uri = s3Service.uploadFile(file);
            entity.setPictureUri(uri);
        } catch (IOException e) {
            throw new BadRequestException("invalid picture");
        }
        entity = repository.save(entity);
        return mapper.map(entity, CustomerDTO.class);
    }

    public void deleteCustomerById(Long id) {
        var entity = repository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("customer not found"));
        if (entity.getRole().equals(ADMIN)) throw new AccessDeniedException("access denied");
        checkAccessForUser(entity);
        var cacheKey = CACHE_CUSTOMER + ":" + entity.getEmail();
        var rentals = rentalRepository.findAllByCustomerIdAndReturnedAtIsNullAndDeletedIsFalse(id);
        if (rentals.isEmpty()) {
            repository.deleteByIdSoftly(id);
            cacheOps.getAndDelete(CACHE_ALL_CUSTOMERS);
            cacheOps.getAndDelete(cacheKey);
        } else {
            throw new BadRequestException("customer have one or more active rentals");
        }
    }

    public Optional<User> getUserWithNonNullPictureUriById(Long id) {
        return repository.findUserByIdAndPictureUriIsNotNullAndDeletedIsFalse(id);
    }
}
