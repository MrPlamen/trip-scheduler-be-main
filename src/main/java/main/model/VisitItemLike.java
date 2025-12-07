package main.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "visit_item_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"visit_item_id", "user_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitItemLike {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "visit_item_id", nullable = false)
    private String visitItemId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private boolean liked;
}


