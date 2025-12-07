package main.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class TripResponse {

    private UUID _id;
    private String title;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private int duration;
    private String imageUrl;
    private String summary;
    private String ownerEmail;
    private LocalDateTime _createdOn;

    private List<String> members;
}

