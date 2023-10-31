package org.ifsul.carhired.api.vehicle;

import org.ifsul.carhired.api.base.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends BaseRepository<Vehicle, Long> {
    List<Vehicle> findAllByModelIdAndDeletedIsFalse(Long id);

    List<Vehicle> findAllByDeletedIsFalseAndAvailableIsTrue();

    Optional<Vehicle> findByIdAndDeletedIsFalseAndAvailableIsTrue(Long id);
}
