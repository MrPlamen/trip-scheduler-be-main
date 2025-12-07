package main.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TripLike {

    @Id
    @GeneratedValue
    private UUID id;

    private String email;
    private UUID userId;
    private UUID tripId;
    private boolean likeValue;
}

