package com.project.scm.services.impl;

import com.project.scm.helper.AppConstants;
import com.project.scm.helper.ResourceNotFoundException;
import com.project.scm.models.User;
import com.project.scm.repositories.UserRepository;
import com.project.scm.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService, Serializable {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @CachePut(value = "users", key = "#user.id")
    public User saveUser(User user) {
        String userId = UUID.randomUUID().toString();
        user.setId(userId);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRoleList(List.of(AppConstants.ROLE_USER));

        logger.info(user.getProvider().toString());
        return userRepository.save(user);
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    @Cacheable(value = "users", key = "#userId")
    public Optional<User> getByIdUser(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    @Cacheable(value = "users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @CachePut(value = "users", key = "#user.id")
    public Optional<User> updateUser(User user) {
        User user2 = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user2.setEmail(user.getEmail());
        user2.setAbout(user.getAbout());
        user2.setContacts(user.getContacts());
        user2.setName(user.getName());
        user2.setProfilePic(user.getProfilePic());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setPassword(user.getPassword());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setEnabled(user.isEnabled());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());

        return Optional.of(userRepository.save(user2));
    }

    @Override
    public boolean isUserExist(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}