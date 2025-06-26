package org.DAD.application.repository;

import org.DAD.domain.entity.Question.ChoiceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChoiceQuestionRepository extends JpaRepository<ChoiceQuestion, UUID> {
}
