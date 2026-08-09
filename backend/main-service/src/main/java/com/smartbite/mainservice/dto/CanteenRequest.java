package com.smartbite.mainservice.dto;

import java.time.LocalTime;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CanteenRequest {
    @NotBlank(message = "Canteen name is required")
    private String canteenName;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Boolean active;
}
