package org.DAD.application.repository;

import org.DAD.domain.entity.User.User;
import org.DAD.domain.entity.User.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findById(UUID id);
    List<User> findByRoleNot(UserRole role);
}
