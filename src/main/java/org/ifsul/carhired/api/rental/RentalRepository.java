package org.ifsul.carhired.api.rental;

import org.ifsul.carhired.api.base.BaseRepository;

import java.util.List;

public interface RentalRepository extends BaseRepository<Rental, Long> {
    List<Rental> findAllByReturnedAtIsNullAndDeletedIsFalse();
    List<Rental> findAllByCustomerIdAndReturnedAtIsNullAndDeletedIsFalse(Long id);
    List<Rental> findAllByReturnedAtIsNotNullAndDeletedIsFalse();
    List<Rental> findAllByCustomerIdAndReturnedAtIsNotNullAndDeletedIsFalse(Long id);
    List<Rental> findAllByCustomerIdAndDeletedIsFalse(Long id);
}