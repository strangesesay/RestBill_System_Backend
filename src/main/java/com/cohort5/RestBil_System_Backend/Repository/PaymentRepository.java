package com.cohort5.RestBil_System_Backend.Repository;

import com.cohort5.RestBil_System_Backend.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {


    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.bill.id = :billId")

    List<Payment> findByPaidAt(LocalDate date);



    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p " +
            "WHERE p.bill.id = :billId AND p.cancelled = false")
    BigDecimal getTotalPaidByBillId(@Param("billId") Long billId);

    @Query("SELECT p FROM Payment p WHERE p.bill.id = :billId " +
            "ORDER BY p.paidAt DESC")
    List<Payment> findByBillId(@Param("billId") Long billId);
}
