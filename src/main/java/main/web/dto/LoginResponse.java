package main.web.dto;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class LoginResponse {
    private UUID id;
    private String username;
    private String email;
}
