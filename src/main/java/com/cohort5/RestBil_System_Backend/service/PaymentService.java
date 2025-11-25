package com.cohort5.RestBil_System_Backend.service;

import com.cohort5.RestBil_System_Backend.Dto.CreatePaymentDto;
import com.cohort5.RestBil_System_Backend.Model.Payment;
import com.cohort5.RestBil_System_Backend.Model.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {

    public Payment addPayment(Long billId, BigDecimal amount, PaymentMethod method, String remarks);

    public List<Payment> addSplitPayments(Long billId, List<CreatePaymentDto> payments);

    public List<Payment> getPaymentsByBill(Long billId);

    public BigDecimal getTotalPaid(Long billId);

    public BigDecimal getRemainingAmount(Long billId);

    public String getPaymentStatus(Long billId);

    public Payment refund(Long billId, BigDecimal amount, String reason);

    public void deletePayment(Long paymentId);

}
