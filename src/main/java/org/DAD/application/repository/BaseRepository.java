package org.DAD.application.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    List<T> findAll(Specification<T> spec, Integer offset, Integer resultsCount);
}
