package main.repository;

import main.model.TripLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TripLikeRepository extends JpaRepository<TripLike, UUID> {
    List<TripLike> findByTripId(UUID tripId);
    void deleteByTripIdAndEmail(UUID tripId, String email);

    TripLike findByTripIdAndUserId(UUID tripId, UUID userId);

    TripLike findByTripIdAndEmail(UUID tripId, String email);
}
