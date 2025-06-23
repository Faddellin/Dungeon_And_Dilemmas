package org.DAD.domain.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.User.*;
import org.DAD.application.repository.UserRepository;
import org.DAD.application.service.UserService;
import org.DAD.domain.entity.User.User;
import org.DAD.domain.entity.User.UserRole;
import org.apache.coyote.BadRequestException;
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
    public UserModel getProfileById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return UserModel.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .totalPoints( 0)
                .bestUsersGame(null)
                .build();
    }

    @Override
    public UserListModel getAllUsersExceptAdmins() {
        List<User> users = userRepository.findByRoleNot(UserRole.Admin);
        
        return new UserListModel(users.stream()
                .map(user -> UserOtherModel.builder()
                        .id(user.getId())
                        .userName(user.getUserName())
                        .totalPoints(0)
                        .bestUsersGame(null)
                        .build())
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
        return new UserModel(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                0,
                null
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
