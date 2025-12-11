package main.web;

import jakarta.servlet.http.HttpSession;
import main.exception.DomainException;
import main.model.User;
import main.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUserByEmail(@RequestParam String email, HttpSession session) {

        try {
            User user = userService.searchByEmail(email);
            return ResponseEntity.ok(user);
        } catch (DomainException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/block/{userId}")
    public ResponseEntity<?> blockUser(@PathVariable UUID userId) {
        userService.blockUser(userId);
        return ResponseEntity.ok("User blocked");
    }

    @PostMapping("/unblock/{userId}")
    public ResponseEntity<?> unblockUser(@PathVariable UUID userId) {
        userService.unblockUser(userId);
        return ResponseEntity.ok("User unblocked");
    }

    @PostMapping("/role/{userId}")
    public ResponseEntity<?> updateRole(@PathVariable UUID userId, @RequestParam String role) {
        userService.updateRole(userId, role);
        return ResponseEntity.ok("Role updated");
    }
}

