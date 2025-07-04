package org.DAD.domain.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.User.*;
import org.DAD.application.repository.UserRepository;
import org.DAD.application.repository.specification.UserSpecification;
import org.DAD.application.service.ResultService;
import org.DAD.application.service.UserService;
import org.DAD.domain.entity.Quiz.Quiz;
import org.DAD.domain.entity.User.User;
import org.DAD.domain.entity.User.UserRole;
import org.apache.coyote.BadRequestException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResultService resultService;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserModel getProfileById(UUID userId) throws ExceptionWrapper {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            ExceptionWrapper entityNotFoundEx = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundEx.addError("User", "User not found");
            throw entityNotFoundEx;
        }

        User user = userOpt.get();
        
        Integer totalPoints = resultService.getTotalPointsByUserId(userId);
        BestUsersGameModel bestGame = resultService.getBestGameByUserId(userId);
        
        return UserModel.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .totalPoints(totalPoints)
                .bestUsersGame(bestGame)
                .build();
    }

    @Override
    public UserListModel findUsers(String name) {
        List<User> users = null;
        Specification<User> spec = (root, query, criteriaBuilder) -> null;
        spec = spec.and(UserSpecification.addEqualSpec(UserRole.Player, "role"));
        if (name != null && !name.isBlank()) {
            spec = spec.and(UserSpecification.addContainsSpec(name, "userName"));
        }
        users = userRepository.findAll(spec);
        return new UserListModel(users.stream()
                            .map(user -> {
                                Integer totalPoints = resultService.getTotalPointsByUserId(user.getId());
                                BestUsersGameModel bestGame = resultService.getBestGameByUserId(user.getId());
                                
                                return UserOtherModel.builder()
                                    .id(user.getId())
                                    .userName(user.getUserName())
                                    .totalPoints(totalPoints)
                                    .bestUsersGame(bestGame)
                                    .build();
                            })
                            .sorted((user1, user2) -> {
                                Integer points1 = user1.totalPoints != null ? user1.totalPoints : 0;
                                Integer points2 = user2.totalPoints != null ? user2.totalPoints : 0;
                                return points2.compareTo(points1);
                            })
                            .collect(Collectors.toList()));
    }

    @Override
    public UserModel editProfile(UUID userId, UserEditModel editModel) throws ExceptionWrapper {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            ExceptionWrapper entityNotFoundEx = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundEx.addError("User", "User not found");
            throw entityNotFoundEx;
        }
        User user = userOpt.get();

        Optional<User> existingUserOpt = userRepository.findByEmail(editModel.newEmail);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            if (user.getId() != existingUser.getId()) {
                ExceptionWrapper badRequestException = new ExceptionWrapper(new BadRequestException());
                badRequestException.addError("Email", "User with this email already exists");
                throw badRequestException;
            }
        }
        user.setEmail(editModel.newEmail);
        user.setUserName(editModel.newUserName);
        userRepository.save(user);
        
        Integer totalPoints = resultService.getTotalPointsByUserId(userId);
        BestUsersGameModel bestGame = resultService.getBestGameByUserId(userId);
        
        return new UserModel(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                totalPoints,
                bestGame
        );
    }

    @Override
    public void editPassword(UUID userId, UserEditPasswordModel editPasswordModel) throws ExceptionWrapper {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            ExceptionWrapper entityNotFoundEx = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundEx.addError("User", "User not found");
            throw entityNotFoundEx;
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(editPasswordModel.oldPassword, user.getPasswordHash())) {
            ExceptionWrapper badRequestException = new ExceptionWrapper(new BadRequestException());
            badRequestException.addError("Password", "Old password is incorrect");
            throw badRequestException;
        }
        user.setPasswordHash(passwordEncoder.encode(editPasswordModel.newPassword));
        userRepository.save(user);
    }
}
