package org.DAD.domain.service;

import lombok.AllArgsConstructor;
import org.DAD.application.model.User.UserListModel;
import org.DAD.application.model.User.UserModel;
import org.DAD.application.model.User.UserOtherModel;
import org.DAD.application.repository.UserRepository;
import org.DAD.application.service.UserService;
import org.DAD.domain.entity.User.User;
import org.DAD.domain.entity.User.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
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
}
