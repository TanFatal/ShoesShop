package com.example.learningAPISpring.dto.Response;

import com.example.learningAPISpring.entity.Payment;
import com.example.learningAPISpring.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private UUID id;
    private String paymentReference;
    private UUID orderId;
    private Double amount;
    private PaymentStatus paymentStatus;
    private String paymentMethod;
    private String qrCodeUrl;
    private Date paymentDate;
    private Date expiredAt;
    private String paymentDescription;
    private String transactionId;

    public PaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.paymentReference = payment.getPaymentReference();
        this.orderId = payment.getOrder() != null ? payment.getOrder().getId() : null;
        this.amount = payment.getAmount();
        this.paymentStatus = payment.getPaymentStatus();
        this.paymentMethod = payment.getPaymentMethod();
        this.qrCodeUrl = payment.getQrCodeUrl();
        this.paymentDate = payment.getPaymentDate();
        this.expiredAt = payment.getExpiredAt();
        this.paymentDescription = payment.getPaymentDescription();
        this.transactionId = payment.getTransactionId();
    }
}
