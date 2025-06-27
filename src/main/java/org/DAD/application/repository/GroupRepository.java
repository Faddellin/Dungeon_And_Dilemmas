package org.DAD.application.repository;

import jakarta.transaction.Transactional;
import org.DAD.domain.entity.Group.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepository
        extends JpaRepository<ChatGroup, UUID> {
    Optional<ChatGroup> findByCode(String code);
}
