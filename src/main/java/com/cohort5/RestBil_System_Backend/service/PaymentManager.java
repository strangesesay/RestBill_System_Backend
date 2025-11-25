package com.cohort5.RestBil_System_Backend.service;

import com.cohort5.RestBil_System_Backend.Dto.CreatePaymentDto;
import com.cohort5.RestBil_System_Backend.Model.Bill;
import com.cohort5.RestBil_System_Backend.Model.Payment;
import com.cohort5.RestBil_System_Backend.Model.PaymentMethod;
import com.cohort5.RestBil_System_Backend.Repository.BillRepository;
import com.cohort5.RestBil_System_Backend.Repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentManager implements PaymentService{

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillRepository billRepository;


    // 1. Add single payment (most used)
    public Payment addPayment(Long billId, BigDecimal amount, PaymentMethod method, String remarks) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found"));

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setPaidAt(LocalDate.now());
        payment.setBill(bill);
        payment.setRemarks(remarks);

        return paymentRepository.save(payment);
    }

    // 2. Add split payments (e.g., ₹500 cash + ₹300 UPI)
    public List<Payment> addSplitPayments(Long billId, List<CreatePaymentDto> payments) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found"));

        List<Payment> saved = new ArrayList<>();
        for (CreatePaymentDto dto : payments) {
            Payment p = new Payment();
            p.setAmount(dto.amount());
            p.setPaymentMethod(dto.paymentMethod());
            p.setPaidAt(LocalDate.now());
            p.setBill(bill);
            p.setRemarks(dto.remarks());
            saved.add(paymentRepository.save(p));
        }
        return saved;
    }

    // 3. Get all payments for a bill
    public List<Payment> getPaymentsByBill(Long billId) {
        return paymentRepository.findByBillId(billId);
    }

    // 4. Get total paid amount
    public BigDecimal getTotalPaid(Long billId) {
        return paymentRepository.getTotalPaidByBillId(billId);
    }

      // 5. Get remaining amount
    public BigDecimal getRemainingAmount(Long billId) {
        Bill bill = billRepository.findById(billId).orElseThrow();
        BigDecimal paid = getTotalPaid(billId);
        return bill.getTotal().subtract(paid).max(BigDecimal.ZERO);
    }

    // 6. Check payment status
    public String getPaymentStatus(Long billId) {
        BigDecimal remaining = getRemainingAmount(billId);
        if (remaining.compareTo(BigDecimal.ZERO) == 0) return "FULLY_PAID";
        if (getTotalPaid(billId).compareTo(BigDecimal.ZERO) > 0) return "PARTIALLY_PAID";
        return "NOT_PAID";
    }

    // 7. Refund (create negative payment – common in restaurants)
    public Payment refund(Long billId, BigDecimal amount, String reason) {
        Payment refund = new Payment();
        refund.setAmount(amount.negate()); // negative amount
        refund.setPaymentMethod(PaymentMethod.CASH); // or create REFUND enum
        refund.setPaidAt(LocalDate.now());
        refund.setRemarks("REFUND: " + reason);

        Bill bill = billRepository.findById(billId).orElseThrow();
        refund.setBill(bill);

        return paymentRepository.save(refund);
    }

    // 8. Delete wrong payment (only if bill not settled)
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        if ("FULLY_PAID".equals(getPaymentStatus(payment.getBill().getId()))) {
            throw new IllegalStateException("Cannot delete payment from fully settled bill");
        }
        paymentRepository.delete(payment);
    }

    public void cancelPayment(Long paymentId, String reason, String cancelledBy) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Reason is required to cancel a payment");
        }

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        // Optional: Prevent cancelling if bill is already closed/printed
        if ("FULLY_PAID".equals(getPaymentStatus(payment.getBill().getId()))) {
            throw new IllegalStateException("Cannot cancel payment from fully settled bill");
        }

        payment.setCancelled(true);
        payment.setCancelledReason(reason.trim());
        payment.setCancelledBy(cancelledBy);
        payment.setCancelledAt(LocalDateTime.now());

        // Save — no delete!
        paymentRepository.save(payment);
    }
}
