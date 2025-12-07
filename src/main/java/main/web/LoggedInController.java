package main.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import main.model.User;
import main.repository.UserRepository;
import main.service.UserService;
import main.web.dto.EditProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class LoggedInController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public LoggedInController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String getProfilePage(HttpSession session, Model model) {
        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getById(userId);

        EditProfileRequest dto = new EditProfileRequest();
        dto.setUsername(user.getUsername());
//        dto.setAvatarUrl(user.getAvatarUrl());

        model.addAttribute("editProfileRequest", dto);
        model.addAttribute("user", user);

        return "profile";
    }

    @PutMapping("profile")
    public String updateProfile(@Valid EditProfileRequest editProfileRequest,
                                BindingResult bindingResult,
                                HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getById(userId);

        if (bindingResult.hasErrors()) {
            return "profile";
        }

        userService.updateProfile(user, editProfileRequest.getUsername(), editProfileRequest.getAvatarUrl());
        return "redirect:/home";
    }
}
