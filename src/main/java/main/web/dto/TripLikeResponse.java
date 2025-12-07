package main.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripLikeResponse {
    private UUID userId;
    private String email;
}

