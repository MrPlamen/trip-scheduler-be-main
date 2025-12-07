package main.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String avatarUrl;

    @Column(nullable = false)
    private String email;

    @ManyToMany(mappedBy = "members")
    private Set<Trip> trips = new HashSet<>();

    @ManyToMany(mappedBy = "members")
    private Set<VisitItem> visitItems = new HashSet<>();

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private WizardAlignment alignment;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private House house;
//
//    @OneToMany(mappedBy = "wizard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<Spell> spells = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime updatedOn;
}
