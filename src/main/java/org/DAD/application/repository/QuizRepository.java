package org.DAD.application.repository;

import org.DAD.application.model.Quiz.QuizFiltersModel;
import org.DAD.domain.entity.Quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuizRepository
        extends BaseRepository<Quiz, UUID> {
    List<Quiz> findByCreatorId(UUID creatorId);
    List<Quiz> findByFilters(QuizFiltersModel quizFiltersModel);
    Integer getCountByFilters(QuizFiltersModel quizFiltersModel);
}
