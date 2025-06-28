package org.DAD.application.repository;

import org.DAD.domain.entity.Result.Result;
import org.DAD.domain.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResultRepository extends JpaRepository<Result, Result.ResultId> {
    List<Result> findByUser(User user);
    
    @Query("SELECT r FROM Result r WHERE r.user.id = :userId")
    List<Result> findByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT SUM(r.score) FROM Result r WHERE r.user.id = :userId")
    Integer getTotalScoreByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT r FROM Result r WHERE r.user.id = :userId ORDER BY r.score DESC LIMIT 1")
    Result findBestResultByUserId(@Param("userId") UUID userId);
} 