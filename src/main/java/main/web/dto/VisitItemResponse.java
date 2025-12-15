package main.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.model.VisitItem;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class VisitItemResponse {
    private UUID id;
    private UUID tripId;
    private String title;
    private String description;
    private String imageUrl;
    private UUID ownerId;
    private LocalDate date;
    private Set<MemberResponse> members;
//    private Integer order;

    public static VisitItemResponse fromEntity(VisitItem entity) {
        VisitItemResponse dto = new VisitItemResponse();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setImageUrl(entity.getImageUrl());
        dto.setDate(LocalDate.from(entity.getCreatedOn()));
        dto.setOwnerId(entity.get_ownerId());
        dto.setMembers(
                entity.getMembers().stream()
                        .map(MemberResponse::fromUser)
                        .collect(Collectors.toSet())
        );
//        dto.setDate(entity.getDate());
//        dto.setOrder(entity.getOrder());

        if (entity.getTrip() != null) {
            dto.setTripId(entity.getTrip().getId());
        }

        return dto;
    }
}

