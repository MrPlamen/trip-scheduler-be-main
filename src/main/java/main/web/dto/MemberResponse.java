package main.web.dto;

import lombok.*;
import main.model.User;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private UUID id;
    private String email;
    private String username;

    public static MemberResponse fromUser(User user) {
        MemberResponse dto = new MemberResponse();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
