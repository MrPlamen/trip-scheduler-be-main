package main.web;

import main.model.TripLike;
import main.service.TripLikeService;
import main.web.dto.TripLikeResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trips/{tripId}/likes")
public class TripLikeController {

    private final TripLikeService service;

    public TripLikeController(TripLikeService service) {
        this.service = service;
    }

    @GetMapping
    public List<TripLike> getLikes(@PathVariable UUID tripId) {
        return service.getTripLikes(tripId);
    }

    @PostMapping
    public TripLike likeTrip(
            @PathVariable UUID tripId,
            @RequestBody TripLike likeRequest
    ) {
        likeRequest.setTripId(tripId);
        return service.likeTrip(tripId, likeRequest.getUserId(), likeRequest.getEmail());
    }


    @DeleteMapping
    public void unlikeTrip(
            @PathVariable UUID tripId,
            @RequestParam String email
    ) {
        service.unlikeTrip(tripId, email);
    }
}


