package main.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class VisitItemLike {

    @Id
    @GeneratedValue
    private UUID id;

    private String email;
    private UUID userId;
    private UUID tripId;
    private UUID visitItemId;
    private boolean likeValue;
}

