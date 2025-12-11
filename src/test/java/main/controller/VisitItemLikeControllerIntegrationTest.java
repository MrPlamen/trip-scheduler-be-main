package main.controller;


import main.repository.VisitItemLikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VisitItemLikeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VisitItemLikeRepository likeRepository;

    private String visitItemId;
    private String userId;

    @BeforeEach
    void setup() {
        likeRepository.deleteAll();
        visitItemId = UUID.randomUUID().toString();
        userId = UUID.randomUUID().toString();
    }

    @Test
    void toggleLike_createsNewLike() throws Exception {
        mockMvc.perform(post("/visitItemLikes/toggle")
                        .param("visitItemId", visitItemId)
                        .param("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitItemId", is(visitItemId)))
                .andExpect(jsonPath("$.userId", is(userId)))
                .andExpect(jsonPath("$.liked", is(true)));
    }

    @Test
    void toggleLike_togglesExistingLike() throws Exception {
        // First toggle creates the like
        mockMvc.perform(post("/visitItemLikes/toggle")
                        .param("visitItemId", visitItemId)
                        .param("userId", userId))
                .andExpect(status().isOk());

        // Second toggle should flip it to false
        mockMvc.perform(post("/visitItemLikes/toggle")
                        .param("visitItemId", visitItemId)
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.liked", is(false)));
    }


}

