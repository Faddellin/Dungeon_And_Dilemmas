package org.DAD.application.repository.specification;

import org.DAD.domain.entity.User.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> addEqualSpec(Object value, String userAttribute){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(userAttribute), value);
    }
    public static Specification<User> addContainsSpec(Object value, String userAttribute){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(userAttribute), "%" + value + "%");
    }
}
