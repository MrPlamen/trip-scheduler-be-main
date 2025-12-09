package main.web.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private String id;
    private String tripId;
    private String email;
    private String comment;
}


