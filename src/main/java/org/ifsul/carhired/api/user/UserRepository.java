package org.ifsul.carhired.api.user;

import org.ifsul.carhired.api.base.BaseRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findUserByEmailAndDeletedIsFalse(String email);

    Boolean existsUserByEmailAndDeletedIsFalse(String email);

    Optional<User> findUserByIdAndPictureUriIsNotNullAndDeletedIsFalse(Long id);

    List<User> findAllByRoleEqualsAndDeletedIsFalse(Role role);
}