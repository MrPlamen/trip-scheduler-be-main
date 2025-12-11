package main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.model.Trip;
import main.model.User;
import main.repository.TripRepository;
import main.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TripControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpSession session;
    private UUID userId;
    private Trip trip;

    @BeforeEach
    void setup() {
        tripRepository.deleteAll();
        userRepository.deleteAll();

        // Create a test user
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        user = userRepository.save(user);
        userId = user.getId();

        // Create a mock session
        session = new MockHttpSession();
        session.setAttribute("user_id", userId);

        // Create a test trip
        trip = new Trip();
        trip.setTitle("Test Trip");
        trip.setOwnerEmail(user.getEmail());
        trip.getMembers().add(user);
        trip = tripRepository.save(trip);
    }

    @Test
    void trips_returnsUserTrips() throws Exception {
        mockMvc.perform(get("/data/trips").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Trip")));
    }

    @Test
    void getOne_returnsTrip() throws Exception {
        mockMvc.perform(get("/data/trips/{id}", trip.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Trip")));
    }

    @Test
    void createTrip_savesTrip() throws Exception {
        Trip newTrip = new Trip();
        newTrip.setTitle("New Trip");

        mockMvc.perform(post("/data/trips")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTrip)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Trip saved successfully")));

        mockMvc.perform(get("/data/trips").session(session))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void editTrip_updatesTrip() throws Exception {
        trip.setTitle("Updated Title");

        mockMvc.perform(put("/data/trips/{id}", trip.getId())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trip)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Trip updated successfully")));

        mockMvc.perform(get("/data/trips/{id}", trip.getId()))
                .andExpect(jsonPath("$.title", is("Updated Title")));
    }

    @Test
    void deleteTrip_removesTrip() throws Exception {
        mockMvc.perform(delete("/data/trips/{id}", trip.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Trip deleted successfully")));

        mockMvc.perform(get("/data/trips/{id}", trip.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void latestTrips_returnsLatest() throws Exception {
        mockMvc.perform(get("/data/trips/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Trip")));
    }

    @Test
    void trips_returns401IfNotLoggedIn() throws Exception {
        mockMvc.perform(get("/data/trips"))
                .andExpect(status().isUnauthorized());
    }
}

