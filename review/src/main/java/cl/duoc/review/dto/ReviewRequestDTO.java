package cl.duoc.review.dto;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDTO {
    @NotNull(message = "El destinationId es obligatorio")
    private UUID destinationId;

    @NotNull(message = "El rating es obligatorio")
    @Min(1) @Max(5)
    private Integer rating;

    @Size(max = 500, message = "El mensaje tiene un máximo de 500 carácteres")
    private String comment;
}