package org.DAD.application.repository.repositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.DAD.application.model.Quiz.QuizFiltersModel;
import org.DAD.application.repository.BaseRepository;
import org.DAD.application.repository.QuizRepository;
import org.DAD.application.repository.specification.QuizSpecification;
import org.DAD.domain.entity.Quiz.Quiz;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;
import java.util.UUID;

public class QuizRepositoryImpl
        extends BaseRepositoryImpl<Quiz, UUID>
        implements QuizRepository {

    private final EntityManager _entityManager;

    public QuizRepositoryImpl(EntityManager entityManager) {
        super(Quiz.class, entityManager);
        _entityManager = entityManager;
    }

    public List<Quiz> findByFilters(QuizFiltersModel quizFiltersModel) {

        Specification<Quiz> spec = (root, query, criteriaBuilder) -> null;

        if(quizFiltersModel.getTitle() != null) {
            spec = spec.and(QuizSpecification.addContainsSpec(quizFiltersModel.getTitle(), "title"));
        }
        if(quizFiltersModel.getDescription() != null) {
            spec = spec.and(QuizSpecification.addContainsSpec(quizFiltersModel.getDescription(), "description"));
        }
        if(quizFiltersModel.getDifficulty() != null) {
            spec = spec.and(QuizSpecification.addContainsSpec(quizFiltersModel.getDifficulty(), "difficulty"));
        }
        if(quizFiltersModel.getCreatorEmail() != null) {
            spec = spec.and(QuizSpecification.addUserContainsSpec(quizFiltersModel.getCreatorEmail(), "email"));
        }

        return this.findAll(spec,
                (quizFiltersModel.getPage() - 1) * quizFiltersModel.getPageSize(),
                quizFiltersModel.getPageSize());
    }

    public Integer getCountByFilters(QuizFiltersModel quizFiltersModel){
        Specification<Quiz> spec = (root, query, criteriaBuilder) -> null;

        if(quizFiltersModel.getTitle() != null) {
            spec = spec.and(QuizSpecification.addContainsSpec(quizFiltersModel.getTitle(), "title"));
        }
        if(quizFiltersModel.getDescription() != null) {
            spec = spec.and(QuizSpecification.addContainsSpec(quizFiltersModel.getDescription(), "description"));
        }
        if(quizFiltersModel.getDifficulty() != null) {
            spec = spec.and(QuizSpecification.addContainsSpec(quizFiltersModel.getDifficulty(), "difficulty"));
        }
        if(quizFiltersModel.getCreatorEmail() != null) {
            spec = spec.and(QuizSpecification.addUserContainsSpec(quizFiltersModel.getCreatorEmail(), "email"));
        }

        return Math.toIntExact(this.count(spec));
    }


}

