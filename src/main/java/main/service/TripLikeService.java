package main.service;

import main.model.TripLike;
import main.repository.TripLikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TripLikeService {

    private final TripLikeRepository repository;

    public TripLikeService(TripLikeRepository repository) {
        this.repository = repository;
    }

    public List<TripLike> getTripLikes(UUID tripId) {
        return repository.findByTripId(tripId);
    }

    public TripLike likeTrip(UUID tripId, UUID userId, String email) {
        TripLike existing = repository.findByTripIdAndUserId(tripId, userId);
        if (existing != null) {
            return existing;
        }

        TripLike like = new TripLike();
        like.setTripId(tripId);
        like.setUserId(userId);
        like.setEmail(email);
        return repository.save(like);
    }

    public void unlikeTrip(UUID tripId, String email) {
        TripLike like = repository.findByTripIdAndEmail(tripId, email);
        if (like != null) {
            repository.delete(like);
        }
    }
}


