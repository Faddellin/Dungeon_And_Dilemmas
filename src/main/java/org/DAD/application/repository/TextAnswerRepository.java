package org.DAD.application.repository;

import org.DAD.domain.entity.Answer.TextAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TextAnswerRepository extends JpaRepository<TextAnswer, UUID> {
}
