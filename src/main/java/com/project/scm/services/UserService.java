package com.project.scm.services;

import java.util.List;
import java.util.Optional;

import com.project.scm.models.User;

public interface UserService {

    User saveUser(User user);

    void deleteUser(String userId);

    Optional<User> getByIdUser(String userId);

    List<User> getAllUsers();

    Optional<User> updateUser(User user);

    boolean isUserExist(String userId);

    boolean isUserExistByEmail(String email);

    User getUserByEmail(String email);

}