package org.DAD.application.repository.specification;

import jakarta.persistence.criteria.Join;
import org.DAD.domain.entity.Quiz.Quiz;
import org.DAD.domain.entity.User.User;
import org.springframework.data.jpa.domain.Specification;

public class QuizSpecification {

    public static Specification<Quiz> addEqualSpec(Object value, String quizAttribute){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(quizAttribute), value);
    }
    public static Specification<Quiz> addContainsSpec(Object value, String quizAttribute){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(quizAttribute), "%" + value + "%");
    }
    public static Specification<Quiz> addUserContainsSpec(Object value, String userAttribute){
        return (root, query, criteriaBuilder) ->{
            Join<Quiz, User> userJoin = root.join("creator");
            return criteriaBuilder.like(userJoin.get(userAttribute), "%" + value + "%");
        };

    }
}
