package com.smartbite.mainservice.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Long reviewId;
    private Long orderId;
    private Long userId;
    private Long foodId;
    private Integer rating;
    private String comment;
    private LocalDateTime reviewedAt;
}
