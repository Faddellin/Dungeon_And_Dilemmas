package org.DAD.application.repository;

import org.DAD.domain.entity.User.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findById(UUID id);
}
