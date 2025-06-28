package org.DAD.domain.service;

import lombok.AllArgsConstructor;
import org.DAD.application.model.User.BestUsersGameModel;
import org.DAD.application.repository.ResultRepository;
import org.DAD.application.service.ResultService;
import org.DAD.domain.entity.Result.Result;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;

    @Override
    public Integer getTotalPointsByUserId(UUID userId) {
        Integer totalScore = resultRepository.getTotalScoreByUserId(userId);
        return totalScore != null ? totalScore : 0;
    }

    @Override
    public BestUsersGameModel getBestGameByUserId(UUID userId) {
        Result bestResult = resultRepository.findBestResultByUserId(userId);
        
        if (bestResult == null) {
            return null;
        }
        
        return BestUsersGameModel.builder()
                .name(bestResult.getQuiz().getTitle())
                .score(bestResult.getScore())
                .build();
    }
} 