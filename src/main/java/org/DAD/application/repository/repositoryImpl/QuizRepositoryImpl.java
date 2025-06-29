package org.DAD.application.repository.repositoryImpl;

import ch.qos.logback.core.net.server.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.DAD.application.model.Quiz.QuizFiltersModel;
import org.DAD.application.repository.BaseRepository;
import org.DAD.application.repository.QuizRepository;
import org.DAD.application.repository.specification.QuizSpecification;
import org.DAD.domain.entity.Quiz.Quiz;
import org.DAD.domain.entity.User.User;
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

    public List<Quiz> findByCreatorId(UUID creatorId) {

        CriteriaBuilder cb = _entityManager.getCriteriaBuilder();
        CriteriaQuery<Quiz> query = cb.createQuery(Quiz.class);
        Root<Quiz> root = query.from(Quiz.class);

        Join<Quiz, User> userJoin = root.join("creator");
        query.where(cb.equal(userJoin.get("id"), creatorId));

        return _entityManager.createQuery(query)
                .getResultList();
    }

    public List<Quiz> findByFilters(QuizFiltersModel quizFiltersModel) {

        Specification<Quiz> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

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
        spec = spec.and(QuizSpecification.addEqualSpec("Published", "status"));

        return this.findAll(spec,
                (quizFiltersModel.getPage() - 1) * quizFiltersModel.getPageSize(),
                quizFiltersModel.getPageSize());
    }

    public Integer getCountByFilters(QuizFiltersModel quizFiltersModel){
        Specification<Quiz> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();;

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
        spec = spec.and(QuizSpecification.addEqualSpec("Published", "status"));

        return Math.toIntExact(this.count(spec));
    }


}

