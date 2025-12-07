package main.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.*;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class VisitItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String imageUrl;

    @Column
    private LocalDateTime createdOn;

    @Column
    private LocalDateTime updatedOn;

    // === RELATIONSHIPS ===
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToMany
    @JoinTable(
            name = "visit_item_members",
            joinColumns = @JoinColumn(name = "visit_item_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    @PreUpdate
    protected void onUpdate() {
        updatedOn = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
    }
}

