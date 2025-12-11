package main.repository;

import main.model.Trip;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {

    List<Trip> findAllByMembersId(UUID userId);

    List<Trip> findAllByOrderByCreatedOnDesc(PageRequest pageRequest);

    @Modifying
    @Query("DELETE FROM Trip t WHERE t.endDate < :date")
    void deleteTripsOlderThan(LocalDate date);
}

