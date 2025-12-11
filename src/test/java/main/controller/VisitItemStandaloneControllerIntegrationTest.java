package main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.model.Trip;
import main.model.VisitItem;
import main.repository.TripRepository;
import main.repository.VisitItemRepository;
import main.web.dto.VisitItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VisitItemStandaloneControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VisitItemRepository visitItemRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID tripId;
    private UUID visitItemId;

    @BeforeEach
    void setup() {
        visitItemRepository.deleteAll();
        tripRepository.deleteAll();

        // Create a trip
        Trip trip = new Trip();
        trip.setTitle("Test Trip");
        trip = tripRepository.save(trip);
        tripId = trip.getId();

        // Create a visit item
        VisitItem item = new VisitItem();
        item.setTrip(trip);
        item.setTitle("Test Item");
        item.setDescription("Description");
        item = visitItemRepository.save(item);
        visitItemId = item.getId();
    }

    @Test
    void getVisitItemById_returnsItem() throws Exception {
        mockMvc.perform(get("/visit-items/{visitItemId}", visitItemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Item")))
                .andExpect(jsonPath("$.description", is("Description")));
    }

    @Test
    void getAllVisitItems_returnsList() throws Exception {
        mockMvc.perform(get("/visit-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Item")));
    }

    @Test
    void editVisitItem_updatesItem() throws Exception {
        VisitItemRequest request = new VisitItemRequest();
        request.setTripId(tripId);
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");

        mockMvc.perform(put("/visit-items/{visitItemId}", visitItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.description", is("Updated Description")));
    }

    @Test
    void deleteVisitItem_removesItem() throws Exception {
        mockMvc.perform(delete("/visit-items/{visitItemId}", visitItemId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/visit-items/{visitItemId}", visitItemId))
                .andExpect(status().isInternalServerError()); // RuntimeException thrown in service
    }
}

