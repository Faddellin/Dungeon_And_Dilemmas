package org.DAD.application.repository;

import org.DAD.domain.entity.Answer.Answer;
import org.DAD.domain.entity.Question.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {

}
