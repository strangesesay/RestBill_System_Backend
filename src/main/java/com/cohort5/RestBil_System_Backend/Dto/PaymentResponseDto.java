package com.cohort5.RestBil_System_Backend.Dto;

import com.cohort5.RestBil_System_Backend.Model.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentResponseDto(
        Long id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String remarks,
        LocalDate paidAt
) {}
