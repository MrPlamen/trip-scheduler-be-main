package main.service;

import main.model.Trip;
import main.model.User;
import main.repository.TripRepository;
import main.repository.UserRepository;
import main.web.dto.TripResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TripServiceTest {

    private TripRepository tripRepository;
    private UserRepository userRepository;
    private TripService tripService;

    @BeforeEach
    void setup() {
        tripRepository = mock(TripRepository.class);
        userRepository = mock(UserRepository.class);
        tripService = new TripService(tripRepository, userRepository);
    }

    @Test
    void testSaveTrip_ResolvesMembers() {
        User u1 = new User();
        u1.setEmail("a@a.com");
        User u2 = new User();
        u2.setEmail("b@b.com");

        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.of(u1));
        when(userRepository.findByEmail("b@b.com")).thenReturn(Optional.of(u2));
        when(tripRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Trip trip = new Trip();
        trip.setMembers(Set.of(u1, u2));

        Trip saved = tripService.saveTrip(trip);

        assertEquals(2, saved.getMembers().size());
        verify(tripRepository).save(trip);
    }

    @Test
    void testGetTripsForUser() {
        UUID userId = UUID.randomUUID();
        Trip t1 = new Trip();
        t1.setId(UUID.randomUUID());
        t1.setTitle("T1");

        when(tripRepository.findAllByMembersId(userId)).thenReturn(List.of(t1));

        List<TripResponse> result = tripService.getTripsForUser(userId);

        assertEquals(1, result.size());
        assertEquals("T1", result.get(0).getTitle());
    }

    @Test
    void testGetTrip() {
        Trip trip = new Trip();
        trip.setId(UUID.randomUUID());
        trip.setTitle("X");

        when(tripRepository.findById(trip.getId())).thenReturn(Optional.of(trip));

        TripResponse response = tripService.getTrip(trip.getId());

        assertEquals("X", response.getTitle());
    }

    @Test
    void testGetTrip_NotFound() {
        UUID id = UUID.randomUUID();
        when(tripRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> tripService.getTrip(id));
    }

    @Test
    void testUpdateTrip() {
        UUID id = UUID.randomUUID();

        Trip existing = new Trip();
        existing.setId(id);
        existing.setTitle("Old");

        Trip updated = new Trip();
        updated.setTitle("New");
        updated.setCategory("A");
        updated.setImageUrl("img");
        updated.setStartDate(LocalDate.now());
        updated.setEndDate(LocalDate.now().plusDays(1));
        updated.setDuration(5);
        updated.setSummary("S");

        when(tripRepository.findById(id)).thenReturn(Optional.of(existing));
        when(tripRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Trip result = tripService.updateTrip(id, updated);

        assertEquals("New", result.getTitle());
        assertEquals("A", result.getCategory());
        verify(tripRepository).save(existing);
    }

    @Test
    void testDeleteTrip() {
        UUID id = UUID.randomUUID();

        Trip trip = new Trip();
        trip.setId(id);
        trip.setMembers(new HashSet<>(List.of(new User())));

        when(tripRepository.findById(id)).thenReturn(Optional.of(trip));

        tripService.deleteTrip(id);

        assertEquals(0, trip.getMembers().size());
        verify(tripRepository).delete(trip);
    }

    @Test
    void testGetLatestTrips() {
        Trip t1 = new Trip();
        t1.setTitle("A");

        when(tripRepository.findAllByOrderByCreatedOnDesc(PageRequest.of(0, 5)))
                .thenReturn(List.of(t1));

        List<TripResponse> result = tripService.getLatestTrips(5);

        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getTitle());
    }

    @Test
    void testToResponseMapping() {
        Trip trip = new Trip();
        trip.setId(UUID.randomUUID());
        trip.setTitle("T");
        trip.setCategory("Cat");
        trip.setImageUrl("img");
        trip.setStartDate(LocalDate.now());
        trip.setEndDate(LocalDate.now());
        trip.setDuration(3);
        trip.setSummary("Sum");
        trip.setOwnerEmail("o@o.com");
        trip.setCreatedOn(LocalDateTime.now());

        User u = new User();
        u.setId(UUID.randomUUID());
        u.setEmail("x@x.com");
        u.setUsername("X");

        trip.setMembers(Set.of(u));

        when(tripRepository.findById(trip.getId())).thenReturn(Optional.of(trip));

        TripResponse r = tripService.getTrip(trip.getId());

        assertEquals("T", r.getTitle());
        assertEquals(1, r.getMembers().size());
        assertEquals("x@x.com", r.getMembers().get(0).getEmail());
    }
}

