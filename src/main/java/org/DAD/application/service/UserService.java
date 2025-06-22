package org.DAD.application.service;

import org.DAD.application.model.User.UserListModel;
import org.DAD.application.model.User.UserModel;
import org.DAD.application.model.User.UserOtherModel;
import org.DAD.domain.entity.User.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User findByEmail(String email);
    void save(User user);
    Optional<User> findById(UUID id);
    boolean existsByEmail(String email);
    UserModel getProfileById(UUID userId);
    UserListModel getAllUsersExceptAdmins();
}
