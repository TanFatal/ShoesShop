package com.example.learningAPISpring.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name="payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Order order;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date paymentDate;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private String qrCodeUrl;

    @Column(nullable = false)
    private String paymentReference; // Mã tham chiếu để track payment

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date expiredAt;

    @Column(nullable = false)
    private String transactionId; // Mã giao dịch từ bank

    @Column(nullable = false)
    private String bankAccount; // STK nhận tiền

    @Column(nullable = false)
    private String paymentDescription; // Nội dung chuyển khoản


    public Payment(Order order, Double amount, String paymentMethod) {
        this.order = order;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.PENDING;
        this.paymentDate = new Date();
        this.paymentReference = "PAY_" + System.currentTimeMillis() + "_" +
                ThreadLocalRandom.current().nextInt(1000, 9999);

        // Set expired time (15 phút)
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 15);
        this.expiredAt = cal.getTime();
    }
    public boolean isExpired() {
        return expiredAt != null && new Date().after(expiredAt);
    }

    public boolean canBeConfirmed() {
        return PaymentStatus.PENDING.equals(paymentStatus) && !isExpired();
    }
}
