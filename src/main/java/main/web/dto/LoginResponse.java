package main.web.dto;

import lombok.*;
import main.model.Role;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class LoginResponse {
    private UUID id;
    private String username;
    private Role role;
    private String email;
}
