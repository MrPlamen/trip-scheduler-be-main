package main.service;

import main.exception.DomainException;
import main.model.Role;
import main.model.User;
import main.repository.UserRepository;
import main.web.dto.LoginRequest;
import main.web.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService sut; // System Under Test

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        sut = new UserService(userRepository, passwordEncoder);
    }

    // ---------------------------------------------------------
    // createNewUser()
    // ---------------------------------------------------------
    @Test
    void createNewUser_success() {
        RegisterRequest req = new RegisterRequest("john", "john@mail.com", "secret123");

        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(req.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret123")).thenReturn("encoded");

        sut.createNewUser(req);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertEquals("john", saved.getUsername());
        assertEquals("john@mail.com", saved.getEmail());
        assertEquals(Role.USER, saved.getRole());
        assertEquals("encoded", saved.getPassword());
    }

    @Test
    void createNewUser_emailExists_throws() {
        RegisterRequest req = new RegisterRequest("john", "john@mail.com", "secret123");
        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(DomainException.class, () -> sut.createNewUser(req));
    }

    @Test
    void createNewUser_usernameExists_throws() {
        RegisterRequest req = new RegisterRequest("john", "john@mail.com", "secret123");

        when(userRepository.findByUsername(req.getUsername())).thenReturn(Optional.of(new User()));
        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> sut.createNewUser(req));
    }

    // ---------------------------------------------------------
    // login()
    // ---------------------------------------------------------
    @Test
    void login_success() {
        LoginRequest request = new LoginRequest("mail@mail.com", "pass");

        User user = User.builder()
                .email("mail@mail.com")
                .password("encoded")
                .blocked(false)
                .build();

        when(userRepository.findByEmail("mail@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "encoded")).thenReturn(true);

        User result = sut.login(request);
        assertEquals("mail@mail.com", result.getEmail());
    }

    @Test
    void login_wrongPassword_throws() {
        LoginRequest request = new LoginRequest("mail@mail.com", "wrong");

        User user = User.builder()
                .email("mail@mail.com")
                .password("encoded")
                .blocked(false)
                .build();

        when(userRepository.findByEmail("mail@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThrows(DomainException.class, () -> sut.login(request));
    }

    @Test
    void login_userBlocked_throws() {
        LoginRequest request = new LoginRequest("mail@mail.com", "pass");

        User blockedUser = User.builder()
                .email("mail@mail.com")
                .password("encoded")
                .blocked(true)
                .build();

        when(userRepository.findByEmail("mail@mail.com")).thenReturn(Optional.of(blockedUser));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        assertThrows(DomainException.class, () -> sut.login(request));
    }

    // ---------------------------------------------------------
    // getById()
    // ---------------------------------------------------------
    @Test
    void getById_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> sut.getById(id));
    }

    // ---------------------------------------------------------
    // updateProfile()
    // ---------------------------------------------------------
    @Test
    void updateProfile_success() {
        User user = new User();
        user.setUsername("old");
        user.setEmail("old@mail.com");
        user.setAvatarUrl("old.png");

        sut.updateProfile(user, "new", "new@mail.com", "new.png");

        assertEquals("new", user.getUsername());
        assertEquals("new@mail.com", user.getEmail());
        assertEquals("new.png", user.getAvatarUrl());
        verify(userRepository).save(user);
    }

    // ---------------------------------------------------------
    // blockUser() / unblockUser()
    // ---------------------------------------------------------
    @Test
    void blockUser_success() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setBlocked(false);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        sut.blockUser(id);

        assertTrue(user.isBlocked());
        verify(userRepository).save(user);
    }

    @Test
    void unblockUser_success() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setBlocked(true);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        sut.unblockUser(id);

        assertFalse(user.isBlocked());
        verify(userRepository).save(user);
    }

    // ---------------------------------------------------------
    // updateRole()
    // ---------------------------------------------------------
    @Test
    void updateRole_success() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setRole(Role.USER);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        sut.updateRole(id, "admin");

        assertEquals(Role.ADMIN, user.getRole());
        verify(userRepository).save(user);
    }

    // ---------------------------------------------------------
    // searchByEmail()
    // ---------------------------------------------------------
    @Test
    void searchByEmail_notFound_throws() {
        when(userRepository.findByEmail("missing@mail.com")).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> sut.searchByEmail("missing@mail.com"));
    }
}

