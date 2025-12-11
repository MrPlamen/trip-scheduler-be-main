package main.service;

import jakarta.transaction.Transactional;
import main.exception.DomainException;
import main.model.Role;
import main.model.User;
import main.repository.UserRepository;
import main.web.dto.LoginRequest;
import main.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createNewUser(RegisterRequest registerRequest) {

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new DomainException("Email already exists");
        }

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new DomainException("Username already exists");
        }

        if (registerRequest.getPassword().length() < 6) {
            throw new DomainException("Password must be at least 6 characters");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .role(Role.USER)
                .avatarUrl("https://cdn.vectorstock.com/i/1000v/92/16/default-profile-picture-avatar-user-icon-vector-46389216.jpg")
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new DomainException("Incorrect username or password"));

        String rawPassword = loginRequest.getPassword();
        String encodedPassword = user.getPassword();

        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new DomainException("Incorrect username or password");
        }

        if (user.isBlocked()) {
            throw new DomainException("User is blocked");
        }

        return user;
    }

    public User getById(UUID userId) {

        return userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found"));
    }

    @Transactional
    public void updateProfile(User user, String username, String email, String avatarUrl) {
        user.setUsername(username);
        user.setAvatarUrl(avatarUrl);
        user.setEmail(email);
        user.setUpdatedOn(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void blockUser(UUID userId) {
        User user = getById(userId);
        user.setBlocked(true);
        userRepository.save(user);
    }

    @Transactional
    public void unblockUser(UUID userId) {
        User user = getById(userId);
        user.setBlocked(false);
        userRepository.save(user);
    }

    @Transactional
    public void updateRole(UUID userId, String role) {
        User user = getById(userId);
        user.setRole(Role.valueOf(role.toUpperCase()));
        userRepository.save(user);
    }

    public User searchByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new DomainException("User not found"));
    }
}
