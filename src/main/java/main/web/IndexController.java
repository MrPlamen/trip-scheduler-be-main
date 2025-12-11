package main.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import main.model.User;
import main.service.UserService;
import main.web.dto.LoginRequest;
import main.web.dto.LoginResponse;
import main.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class IndexController {

    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/me")
    public ResponseEntity<?> me(HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.getById(userId);

        return ResponseEntity.ok(
                new LoginResponse(user.getId(), user.getUsername(), user.getRole(), user.getEmail())
        );
    }

    @PostMapping("users/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        userService.createNewUser(registerRequest);

        return ResponseEntity.ok("Registered!");
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        User user = userService.login(loginRequest);

        session.setAttribute("user_id", user.getId());
        session.setAttribute("role", user.getRole());

        var authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
        var auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        LoginResponse loginResponse = new LoginResponse(user.getId(), user.getUsername(), user.getRole(), user.getEmail());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/users/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> body, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) return ResponseEntity.status(401).build();

        User user = userService.getById(userId);

        String username = body.get("username");
        String email = body.get("email");
        String avatarUrl = body.get("avatarUrl");

        userService.updateProfile(user, username, email, avatarUrl);

        return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));
    }

}
