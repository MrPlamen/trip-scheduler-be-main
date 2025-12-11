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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
                new LoginResponse(user.getId(), user.getUsername(), user.getEmail())
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

    @PostMapping("users/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {

        System.out.println(loginRequest.getEmail());
        System.out.println(loginRequest.getPassword());

        User user = userService.login(loginRequest);
        session.setAttribute("user_id", user. getId());

        LoginResponse loginResponse = new LoginResponse(user.getId(), user.getUsername(), user.getEmail());

        return ResponseEntity.ok(loginResponse);
    }
}
