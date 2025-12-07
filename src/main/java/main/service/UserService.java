package main.service;

import jakarta.transaction.Transactional;
import main.exception.DomainException;
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

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .avatarUrl("https://cdn.vectorstock.com/i/1000v/92/16/default-profile-picture-avatar-user-icon-vector-46389216.jpg")
//                .alignment(registerRequest.getAlignment())
//                .house(registerRequest.getHouse())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
//                .spells(new ArrayList<>())
                .build();

//        SpellDetails spellDetails = spellsProperties.getRandomByMinLearnedZero();
//
//        Spell initialSpell = Spell.builder()
//                .code(spellDetails.getCode())
//                .name(spellDetails.getName())
//                .description(spellDetails.getDescription())
//                .category(spellDetails.getCategory())
//                .alignment(spellDetails.getAlignment())
//                .image(spellDetails.getImage())
//                .power(spellDetails.getPower())
//                .createdOn(LocalDateTime.now())
//                .user(user)
//                .build();

//        user.getSpells().add(initialSpell);

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

        return user;
    }

    public User getById(UUID userId) {

        return userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found"));
    }

    @Transactional
    public void updateProfile(User user, String username, String avatarUrl) {
        user.setUsername(username);
//        user.setAvatarUrl(avatarUrl);
        user.setUpdatedOn(LocalDateTime.now());
        userRepository.save(user);
    }

    public void changeAlignmentToDark(User user) {
//        user.setAlignment(UserAlignment.DARK);
        userRepository.save(user);
    }

//    public Map<String, List<SpellDetails>> getUnlearnedSpells(User user) {
//        Set<String> learnedCodes = user.getSpells().stream()
//                .map(Spell::getCode)
//                .collect(Collectors.toSet());
//
//        List<SpellDetails> unlearned = spellsProperties.getSpells().stream()
//                .filter(s -> !learnedCodes.contains(s.getCode()))
//                .toList();
//
//        List<SpellDetails> available = unlearned.stream()
//                .filter(s -> user.getSpells().size() >= s.getMinLearned())
//                .toList();
//
//        List<SpellDetails> locked = unlearned.stream()
//                .filter(s -> user.getSpells().size() < s.getMinLearned())
//                .toList();
//
//        Map<String, List<SpellDetails>> result = new HashMap<>();
//        result.put("available", available);
//        result.put("locked", locked);
//        return result;
//    }
}
