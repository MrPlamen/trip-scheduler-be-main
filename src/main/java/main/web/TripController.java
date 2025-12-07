package main.web;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import main.model.User;
import main.service.TripService;
import main.service.UserService;
import main.web.dto.TripResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/data")
public class TripController {

    private final TripService tripService;

    private final UserService userService;

    @Autowired
    public TripController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

//    @GetMapping("/mine")
//    public List<TripResponse> getMyTrips(@RequestParam String email) {
//        return tripService.getTripsForUser(email);
//    }

    @GetMapping("/trips")
    public ResponseEntity<?> trips(HttpSession session) {

        System.out.println("Good");

        UUID userId = (UUID) session.getAttribute("user_id");
        System.out.println("user id : " + userId);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        List<TripResponse> tripsForUser = tripService.getTripsForUser(userId);
        tripsForUser.forEach(trip -> System.out.println(trip.toString()));

        return ResponseEntity.ok(tripsForUser);
    }

    @GetMapping("/trips/{id}")
    public TripResponse getOne(@PathVariable UUID id) {

        System.out.println("Hit the road, Jack");
        return tripService.getTrip(id);
    }
}

