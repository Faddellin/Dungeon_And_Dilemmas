package org.DAD.application.service;

import org.DAD.application.model.User.BestUsersGameModel;

import java.util.UUID;

public interface ResultService {
    Integer getTotalPointsByUserId(UUID userId);
    BestUsersGameModel getBestGameByUserId(UUID userId);
} 