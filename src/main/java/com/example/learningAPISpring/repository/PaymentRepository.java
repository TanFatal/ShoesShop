package com.example.learningAPISpring.repository;

import com.example.learningAPISpring.entity.Order;
import com.example.learningAPISpring.entity.Payment;
import com.example.learningAPISpring.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByPaymentReference(String paymentReference);
    Optional<Payment> findByOrder(Order order);
    Optional<Payment> findByOrderId(UUID orderId);
    List<Payment> findByPaymentStatusAndExpiredAtBefore(PaymentStatus status, Date dateTime);

    @Query("SELECT p FROM Payment p WHERE p.paymentStatus = 'PENDING' AND p.expiredAt < :now")
    List<Payment> findExpiredPendingPayments(@Param("now") Date now);

    @Query("SELECT p FROM Payment p WHERE p.paymentMethod = 'QR_TRANSFER' AND p.paymentStatus = 'PENDING'")
    List<Payment> findPendingQRPayments();

}
