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
    private LocalDate date;
    private String imageUrl;
    private Integer order;
    private UUID tripId;
}

