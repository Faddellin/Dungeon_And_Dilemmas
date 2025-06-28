package org.DAD.application.service;

import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.User.UserEditModel;
import org.DAD.application.model.User.UserEditPasswordModel;
import org.DAD.application.model.User.UserListModel;
import org.DAD.application.model.User.UserModel;
import org.DAD.domain.entity.User.User;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> findByEmail(String email);
    void save(User user);
    Optional<User> findById(UUID id);
    boolean existsByEmail(String email);
    UserModel getProfileById(UUID userId) throws ExceptionWrapper;
    UserListModel findUsers(String name);
    UserModel editProfile(UUID userId, UserEditModel editModel) throws ExceptionWrapper;
    void editPassword(UUID userId, UserEditPasswordModel editPasswordModel) throws ExceptionWrapper;
}
