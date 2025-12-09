package main.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private Integer duration;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false, unique = true)
    private String ownerEmail;

    @Column
    private LocalDateTime createdOn;

    @Column
    private LocalDateTime updatedOn;

    // === RELATIONSHIPS ===
    @ManyToMany
    @JoinTable(
            name = "trip_members",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitItem> visitItems = new ArrayList<>();

    @PreUpdate
    protected void onUpdate() {
        updatedOn = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
    }
}
