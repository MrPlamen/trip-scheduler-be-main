package main.service;

import main.model.Trip;
import main.model.VisitItem;
import main.repository.TripRepository;
import main.repository.VisitItemRepository;
import main.web.dto.VisitItemRequest;
import main.web.dto.VisitItemResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VisitItemService {

    private final VisitItemRepository visitItemRepository;
    private final TripRepository tripRepository;

    public VisitItemService(VisitItemRepository visitItemRepository, TripRepository tripRepository) {
        this.visitItemRepository = visitItemRepository;
        this.tripRepository = tripRepository;
    }

    public List<VisitItemResponse> getVisitItemsByTripId(UUID tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        return trip.getVisitItems()
                .stream()
                .map(VisitItemResponse::fromEntity)
                .toList();
    }

    public VisitItemResponse getVisitItem(UUID tripId, UUID visitItemId) {
        VisitItem item = visitItemRepository.findByIdAndTripId(visitItemId, tripId)
                .orElseThrow(() -> new RuntimeException("Visit item not found"));

        return VisitItemResponse.fromEntity(item);
    }

    public VisitItemResponse createVisitItem(UUID tripId, VisitItemRequest request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        VisitItem item = new VisitItem();
        item.setTrip(trip);
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setImageUrl(request.getImageUrl());
        item.set_ownerId(request.get_ownerId());
//        item.setDate(request.getDate());

        visitItemRepository.save(item);

        return VisitItemResponse.fromEntity(item);
    }

    public VisitItemResponse editVisitItem(UUID tripId, UUID visitItemId, VisitItemRequest request) {
        VisitItem item = visitItemRepository.findByIdAndTripId(visitItemId, tripId)
                .orElseThrow(() -> new RuntimeException("Visit item not found"));

        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
//        item.setDate(request.getDate());

        visitItemRepository.save(item);

        return VisitItemResponse.fromEntity(item);
    }

    public void deleteVisitItem(UUID visitItemId) {
        VisitItem item = visitItemRepository.findById(visitItemId)
                .orElseThrow(() -> new RuntimeException("Visit item not found"));

        item.getMembers().clear();

        visitItemRepository.delete(item);
    }

    public VisitItemResponse getVisitItemById(UUID visitItemId) {
        VisitItem item = visitItemRepository.findById(visitItemId)
                .orElseThrow(() -> new RuntimeException("Visit item not found"));

        return VisitItemResponse.fromEntity(item);
    }

    public List<VisitItemResponse> getAllVisitItems() {
        return visitItemRepository.findAll()
                .stream()
                .map(VisitItemResponse::fromEntity)
                .toList();
    }
}

