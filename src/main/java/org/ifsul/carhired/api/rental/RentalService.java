package org.ifsul.carhired.api.rental;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ifsul.carhired.api.rental.dto.RentalCreateDTO;
import org.ifsul.carhired.api.rental.dto.RentalDTO;
import org.ifsul.carhired.api.rental.dto.RentalReturnDTO;
import org.ifsul.carhired.api.system.exception.BadRequestException;
import org.ifsul.carhired.api.system.security.JwtService;
import org.ifsul.carhired.api.user.UserRepository;
import org.ifsul.carhired.api.user.customer.Customer;
import org.ifsul.carhired.api.vehicle.VehicleRepository;
import org.ifsul.carhired.api.vehicle.dto.VehicleDTO;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.ifsul.carhired.api.system.security.Utils.getAuthenticatedUser;
import static org.ifsul.carhired.api.user.Role.ADMIN;

@Service
@Validated
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository repository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final JwtService jwtService;
    private final RentalMapper rentalMapper;
    private final ModelMapper mapper;


    public List<VehicleDTO> findAvailableVehicles() {
        return vehicleRepository
                .findAllByDeletedIsFalseAndAvailableIsTrue()
                .stream()
                .map(entity -> mapper.map(entity, VehicleDTO.class))
                .toList();
    }

    public List<RentalDTO> findAllRentals() {
        var authUser = getAuthenticatedUser();
        if (authUser.getRole().equals(ADMIN)) {
            return repository.findAllByDeletedIsFalse()
                    .stream()
                    .map(rentalMapper::map)
                    .toList();
        } else {
            return repository.findAllByCustomerIdAndDeletedIsFalse(authUser.getId())
                    .stream()
                    .map(rentalMapper::map)
                    .toList();
        }
    }

    public RentalDTO findRentalById(Long id) {
        var authUser = getAuthenticatedUser();
        var rental = repository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("rental not found"));
        if (authUser.getRole().equals(ADMIN)) {
            return rentalMapper.map(rental);
        }
        if (!Objects.equals(rental.getCustomer().getId(), authUser.getId())) {
            throw new AccessDeniedException("access denied");
        }
        return rentalMapper.map(rental);
    }

    public List<RentalDTO> findAllRentalsStatus(@NotNull RentalStatus status) {
        var authUser = getAuthenticatedUser();

        if (authUser.getRole().equals(ADMIN)) {
            if (status.equals(RentalStatus.ACTIVE)) {
                return repository
                        .findAllByReturnedAtIsNullAndDeletedIsFalse()
                        .stream()
                        .map(rentalMapper::map)
                        .toList();
            } else if (status.equals(RentalStatus.FINISHED)) {
                return repository
                        .findAllByReturnedAtIsNotNullAndDeletedIsFalse()
                        .stream()
                        .map(rentalMapper::map)
                        .toList();
            } else throw new BadRequestException("invalid status");
        }
        if (status.equals(RentalStatus.ACTIVE)) {
            return repository
                    .findAllByCustomerIdAndReturnedAtIsNullAndDeletedIsFalse(authUser.getId())
                    .stream()
                    .map(rentalMapper::map)
                    .toList();
        } else if (status.equals(RentalStatus.FINISHED)) {
            return repository
                    .findAllByCustomerIdAndReturnedAtIsNotNullAndDeletedIsFalse(authUser.getId())
                    .stream()
                    .map(rentalMapper::map)
                    .toList();
        } else throw new BadRequestException("invalid status");
    }

    public RentalDTO createRental(@Valid @NonNull RentalCreateDTO request, String token) {
        var email = jwtService.extractUsername(token);
        var vehicle = vehicleRepository.findByIdAndDeletedIsFalseAndAvailableIsTrue(request.getVehicleId())
                .orElseThrow(() -> new OpenApiResourceNotFoundException("vehicle unavailable"));
        var user = userRepository.findUserByEmailAndDeletedIsFalse(email)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("customer not found"));
        vehicle.setAvailable(false);
        var entity = Rental
                .builder()
                .dueDate(LocalDate.now().plusDays(request.getPeriod()))
                .customer(mapper.map(user, Customer.class))
                .vehicle(vehicle)
                .deleted(false)
                .build();
        entity = repository.save(entity);
        return rentalMapper.map(entity);
    }

    public RentalDTO returnRental(@NonNull Long id, @Valid @NotNull RentalReturnDTO request) {
        var entity = repository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("rental not found"));
        if (entity.getReturnedAt() != null) {
            throw new BadRequestException("rental already closed");
        }
        var vehicle = entity.getVehicle();
        if (request.getMileage() < vehicle.getMileage()) {
            throw new BadRequestException("mileage: is too low");
        }
        vehicle.setAvailable(true);
        vehicle.setMileage(request.getMileage());
        entity.setVehicle(vehicle);
        entity.setReturnedAt(LocalDate.now());
        entity = repository.save(entity);
        return rentalMapper.map(entity);
    }

    @Transactional
    public void deleteRentalById(Long id) {
        var entity = repository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("rental not found"));
        if (entity.getVehicle().getAvailable()) {
            repository.deleteByIdSoftly(id);
        } else {
            throw new BadRequestException("related vehicle has an active lease");
        }
    }
}
