package cl.duoc.review.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {
    private Long id;
    private UUID destinationId;
    private UUID userId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}