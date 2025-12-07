package main.service;

import lombok.RequiredArgsConstructor;
import main.model.Trip;
import main.model.User;
import main.repository.TripRepository;
import main.web.dto.TripResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    public List<TripResponse> getTripsForUser(UUID userId) {
        List<Trip> trips = tripRepository.findAllByMembersId(userId);

        System.out.println("trips USER id : " + userId);

        return trips.stream()
                .map(t -> TripResponse.builder()
                        ._id(t.getId())
                        .title(t.getTitle())
                        .category(t.getCategory())
                        .startDate(t.getStartDate())
                        .endDate(t.getEndDate())
                        .duration(t.getDuration())
                        .imageUrl(t.getImageUrl())
                        .summary(t.getSummary())
                        .ownerEmail(t.getOwnerEmail())
                        ._createdOn(t.getCreatedOn())
                        .members(
                                t.getMembers()
                                        .stream()
                                        .map(User::getEmail)
                                        .toList()
                        )
                        .build()
                )
                .toList();
    }

    public TripResponse getTrip(UUID id) {
        return tripRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow();
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
                .members(
                        trip.getMembers().stream()
                                .map(User::getEmail)
                                .toList()
                )
                .build();
    }

}
