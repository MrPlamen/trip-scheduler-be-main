package main.service;

import lombok.RequiredArgsConstructor;
import main.model.Trip;
import main.model.User;
import main.repository.TripRepository;
import main.repository.UserRepository;
import main.web.dto.TripResponse;
import main.web.dto.MemberResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    @Transactional
    public Trip saveTrip(Trip trip) {

        if (trip.getMembers() != null && !trip.getMembers().isEmpty()) {
            Set<User> existingMembers = trip.getMembers().stream()
                    .map(member -> userRepository.findByEmail(member.getEmail()).orElse(null))
                    .filter(user -> user != null)
                    .collect(Collectors.toSet());
            trip.setMembers(existingMembers);
        }

        return tripRepository.save(trip);
    }

    public List<TripResponse> getTripsForUser(UUID userId) {
        List<Trip> trips = tripRepository.findAllByMembersId(userId);
        return trips.stream().map(this::toResponse).toList();
    }

    public TripResponse getTrip(UUID id) {
        return tripRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
    }

    @Transactional
    public Trip updateTrip(UUID tripId, Trip updatedTrip) {
        Trip existingTrip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        existingTrip.setTitle(updatedTrip.getTitle());
        existingTrip.setCategory(updatedTrip.getCategory());
        existingTrip.setImageUrl(updatedTrip.getImageUrl());
        existingTrip.setStartDate(updatedTrip.getStartDate());
        existingTrip.setEndDate(updatedTrip.getEndDate());
        existingTrip.setDuration(updatedTrip.getDuration());
        existingTrip.setSummary(updatedTrip.getSummary());

        return saveTrip(existingTrip);
    }

    public void deleteTrip(UUID tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        trip.getMembers().clear();

        tripRepository.delete(trip);
    }

    private TripResponse toResponse(Trip trip) {
        return TripResponse.builder()
                ._id(trip.getId())
                .title(trip.getTitle())
                .category(trip.getCategory())
                .imageUrl(trip.getImageUrl())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .duration(trip.getDuration())
                .summary(trip.getSummary())
                .ownerEmail(trip.getOwnerEmail())
                ._createdOn(trip.getCreatedOn())
                .members(trip.getMembers().stream()
                        .map(MemberResponse::fromUser)
                        .toList())
                .build();
    }

    public List<TripResponse> getLatestTrips(int limit) {
        List<Trip> trips = tripRepository.findAllByOrderByCreatedOnDesc(PageRequest.of(0, limit));

        return trips.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
