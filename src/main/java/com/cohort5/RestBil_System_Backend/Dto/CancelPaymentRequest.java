package com.cohort5.RestBil_System_Backend.Dto;

import jakarta.validation.constraints.NotBlank;

public record CancelPaymentRequest(
        @NotBlank(message = "Reason is required" )
        String reason
) {
}
