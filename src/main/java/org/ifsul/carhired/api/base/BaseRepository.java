package org.ifsul.carhired.api.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseModelEntity, ID extends Serializable> extends JpaRepository<T, ID> {
    List<T> findAllByDeletedIsFalse();

    Optional<T> findByIdAndDeletedIsFalse(ID id);

    default void deleteByIdSoftly(ID id) {
        findById(id).ifPresent(entity -> {
            if (!entity.getDeleted()) {
                entity.setDeletedAt(LocalDateTime.now());
                entity.setDeleted(true);
                save(entity);
            }
        });
    }
}
