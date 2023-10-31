package org.ifsul.carhired.api.model;

import org.ifsul.carhired.api.base.BaseRepository;

import java.util.List;

public interface ModelRepository extends BaseRepository<Model, Long> {
    List<Model> findAllByAutomakerIdAndDeletedIsFalse(Long id);
}