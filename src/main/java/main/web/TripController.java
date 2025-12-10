package main.web;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import main.model.User;
import main.service.TripService;
import main.service.UserService;
import main.web.dto.TripResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import main.model.Trip;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final UserService userService;

    @GetMapping("/trips")
    public ResponseEntity<?> trips(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) return ResponseEntity.status(401).build();

        List<TripResponse> tripsForUser = tripService.getTripsForUser(userId);
        return ResponseEntity.ok(tripsForUser);
    }

    @GetMapping("/trips/{id}")
    public ResponseEntity<?> getOne(@PathVariable UUID id) {
        try {
            TripResponse trip = tripService.getTrip(id);
            return ResponseEntity.ok(trip);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Trip not found");
        }
    }

    @PostMapping("/trips")
    public ResponseEntity<?> createTrip(@RequestBody Trip trip, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) return ResponseEntity.status(401).build();

        User owner = userService.getById(userId);
        trip.setOwnerEmail(owner.getEmail());

        trip.getMembers().add(owner);

        try {
            tripService.saveTrip(trip);
            return ResponseEntity.ok("Trip saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating trip: " + e.getMessage());
        }
    }
}

