package main.web.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitItemRequest {
    private String title;
    private String description;
    private UUID _ownerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate date;
    private String imageUrl;
    private Integer order;
    private UUID tripId;
}

