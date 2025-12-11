package main.service;

import main.model.TripLike;
import main.repository.TripLikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TripLikeServiceTest {

    private TripLikeRepository repository;
    private TripLikeService service;

    @BeforeEach
    void setup() {
        repository = mock(TripLikeRepository.class);
        service = new TripLikeService(repository);
    }

    @Test
    void testGetTripLikes() {
        UUID tripId = UUID.randomUUID();
        List<TripLike> likes = List.of(new TripLike(), new TripLike());

        when(repository.findByTripId(tripId)).thenReturn(likes);

        List<TripLike> result = service.getTripLikes(tripId);

        assertEquals(2, result.size());
        assertEquals(likes, result);
        verify(repository, times(1)).findByTripId(tripId);
    }

    @Test
    void testLikeTrip_NewLike() {
        UUID tripId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(repository.findByTripIdAndUserId(tripId, userId)).thenReturn(null);

        TripLike saved = new TripLike();
        saved.setTripId(tripId);
        saved.setUserId(userId);
        saved.setEmail("email@test.com");

        when(repository.save(any(TripLike.class))).thenReturn(saved);

        TripLike result = service.likeTrip(tripId, userId, "email@test.com");

        assertEquals(tripId, result.getTripId());
        assertEquals(userId, result.getUserId());
        assertEquals("email@test.com", result.getEmail());
        verify(repository, times(1)).findByTripIdAndUserId(tripId, userId);
        verify(repository, times(1)).save(any(TripLike.class));
    }

    @Test
    void testLikeTrip_AlreadyExists() {
        UUID tripId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        TripLike existing = new TripLike();
        existing.setTripId(tripId);
        existing.setUserId(userId);

        when(repository.findByTripIdAndUserId(tripId, userId)).thenReturn(existing);

        TripLike result = service.likeTrip(tripId, userId, "email@test.com");

        assertEquals(existing, result);
        verify(repository, times(1)).findByTripIdAndUserId(tripId, userId);
        verify(repository, never()).save(any());
    }

    @Test
    void testUnlikeTrip() {
        UUID tripId = UUID.randomUUID();
        String email = "test@test.com";

        TripLike like = new TripLike();
        when(repository.findByTripIdAndEmail(tripId, email)).thenReturn(like);

        service.unlikeTrip(tripId, email);

        verify(repository, times(1)).delete(like);
    }

    @Test
    void testUnlikeTrip_NotFound() {
        UUID tripId = UUID.randomUUID();
        String email = "test@test.com";

        when(repository.findByTripIdAndEmail(tripId, email)).thenReturn(null);

        service.unlikeTrip(tripId, email);

        verify(repository, never()).delete(any());
    }
}

