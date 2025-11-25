package com.cohort5.RestBil_System_Backend.Dto;

import com.cohort5.RestBil_System_Backend.Model.PaymentMethod;

import java.math.BigDecimal;

public record CreatePaymentDto(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String remarks
) {}
