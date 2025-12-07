package main.repository;

import main.model.VisitItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VisitItemRepository extends JpaRepository<VisitItem, UUID> {
    Optional<VisitItem> findByIdAndTripId(UUID id, UUID tripId);
}
