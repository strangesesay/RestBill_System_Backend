package com.cohort5.RestBil_System_Backend.controller;

import com.cohort5.RestBil_System_Backend.Dto.CancelPaymentRequest;
import com.cohort5.RestBil_System_Backend.Dto.CreatePaymentDto;
import com.cohort5.RestBil_System_Backend.Model.Payment;
import com.cohort5.RestBil_System_Backend.Model.User;
import com.cohort5.RestBil_System_Backend.service.PaymentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills/{billId}/payments")
public class PaymentController {

    @Autowired
    private PaymentManager paymentManager;

    @PostMapping
    public ResponseEntity<Payment> addPayment(@PathVariable Long billId, @RequestBody CreatePaymentDto dto){
        Payment payment = paymentManager.addPayment(billId, dto.amount(), dto.paymentMethod(), dto.remarks());
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/split")
    public ResponseEntity<List<Payment>> splitPayment(@PathVariable Long billId, @RequestBody List<CreatePaymentDto> payments){
        return ResponseEntity.ok(paymentManager.addSplitPayments(billId, payments));
    }

    @GetMapping
    public List<Payment> getPayments(@PathVariable Long billId) {
        return paymentManager.getPaymentsByBill(billId);
    }

    @GetMapping("/summary")
    public Map<String, Object> getSummary(@PathVariable Long billId) {
        return Map.of(
                "totalPaid", paymentManager.getTotalPaid(billId),
                "remaining", paymentManager.getRemainingAmount(billId),
                "status", paymentManager.getPaymentStatus(billId)
        );
    }

   /* @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long paymentId) {
        paymentManager.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> cancelPayment(
            @PathVariable Long billId,
            @PathVariable Long paymentId,
            @RequestBody CancelPaymentRequest request,   // contains reason
            @AuthenticationPrincipal User user) {       // or get from security context

        String cashierName = (user != null) ? user.getUsername() : "Cashier";

        paymentManager.cancelPayment(
                paymentId,
                request.reason(),
                cashierName
        );

        return ResponseEntity.noContent().build();
    } */

    // THIS IS THE ONLY DELETE/CANCEL ENDPOINT YOU NEED NOW
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> cancelPayment(
            @PathVariable Long billId,
            @PathVariable Long paymentId,
            @RequestBody CancelPaymentRequest request,
            Authentication authentication) {  // Get logged-in user

        String cashierName = "Cashier"; // fallback
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            cashierName = user.getUsername();
        }

        paymentManager.cancelPayment(paymentId, request.reason(), cashierName);
        return ResponseEntity.noContent().build();
    }
}
