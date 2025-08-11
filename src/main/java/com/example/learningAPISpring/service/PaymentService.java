package com.example.learningAPISpring.service;


import com.example.learningAPISpring.dto.Request.QRPaymentRequest;
import com.example.learningAPISpring.dto.Response.PaymentResponse;
import com.example.learningAPISpring.entity.Order;
import com.example.learningAPISpring.entity.Payment;
import com.example.learningAPISpring.entity.PaymentStatus;
import com.example.learningAPISpring.exception.InvalidPaymentStateException;
import com.example.learningAPISpring.exception.OrderNotFoundException;
import com.example.learningAPISpring.exception.PaymentExpiredException;
import com.example.learningAPISpring.exception.PaymentNotFoundException;
import com.example.learningAPISpring.repository.OrderRepository;
import com.example.learningAPISpring.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository; // Giả sử bạn có OrderRepository

    @Value("${spring.payment.tpbank.account-number}")
    private String tpBankAccountNumber;

    @Value("${spring.payment.tpbank.bank-code}")
    private String bankCode;

    @Value("${spring.payment.qr.base-url}")
    private String qrBaseUrl;

    public PaymentResponse createQRPayment(QRPaymentRequest request) {
        // Tìm order
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + request.getOrderId()));

        // Kiểm tra xem order đã có payment chưa
        Optional<Payment> existingPayment = paymentRepository.findByOrder(order);
        if (existingPayment.isPresent() &&
                existingPayment.get().getPaymentStatus() == PaymentStatus.PENDING) {
            // Nếu đã có payment pending, trả về payment đó
            Payment payment = existingPayment.get();
            if (!payment.isExpired()) {
                return new PaymentResponse(payment);
            }
        }

        // Tạo payment mới
        Payment payment = new Payment(order, request.getAmount(), "QR_TRANSFER");
        payment.setBankAccount(tpBankAccountNumber);
        payment.setPaymentDescription(request.getDescription() != null ?
                request.getDescription() : "Thanh toan don hang " + order.getId());

        // Tạo QR code URL
        String qrUrl = generateQRCodeUrl(payment);
        payment.setQrCodeUrl(qrUrl);

        // Lưu vào database
        Payment savedPayment = paymentRepository.save(payment);

        log.info("Created QR payment: {} for order: {} with amount: {}",
                payment.getPaymentReference(), order.getId(), request.getAmount());

        return new PaymentResponse(savedPayment);
    }

    private String generateQRCodeUrl(Payment payment) {
        try {
            String description = URLEncoder.encode(
                    payment.getPaymentDescription() + " - " + payment.getPaymentReference(),
                    StandardCharsets.UTF_8.toString()
            );

            return String.format("%s/%s-%s-compact2.png?amount=%.0f&addInfo=%s",
                    qrBaseUrl,
                    bankCode,
                    tpBankAccountNumber,
                    payment.getAmount(),
                    description
            );
        } catch (UnsupportedEncodingException e) {
            log.error("Error encoding QR URL", e);
            throw new RuntimeException("Error generating QR code", e);
        }
    }

    public PaymentResponse getPaymentStatus(String paymentReference) {
        Payment payment = paymentRepository.findByPaymentReference(paymentReference)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + paymentReference));

        // Kiểm tra expired
        if (payment.getPaymentStatus() == PaymentStatus.PENDING && payment.isExpired()) {
            payment.setPaymentStatus(PaymentStatus.EXPIRED);
            paymentRepository.save(payment);
        }

        return new PaymentResponse(payment);
    }

    public PaymentResponse getPaymentByOrderId(UUID orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for order: " + orderId));

        return new PaymentResponse(payment);
    }

    public PaymentResponse confirmPayment(String paymentReference, String transactionId) {
        Payment payment = paymentRepository.findByPaymentReference(paymentReference)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + paymentReference));

        if (!payment.canBeConfirmed()) {
            if (payment.isExpired()) {
                throw new PaymentExpiredException("Payment has expired");
            }
            throw new InvalidPaymentStateException("Payment is not in confirmable state");
        }

        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setTransactionId(transactionId);
        payment.setPaymentDate(new Date()); // Update payment date to actual payment time

        Payment savedPayment = paymentRepository.save(payment);

        log.info("Payment confirmed: {} with transaction: {}", paymentReference, transactionId);

        // Trigger order completion event
        publishOrderCompletedEvent(payment.getOrder().getId());

        return new PaymentResponse(savedPayment);
    }

    @EventListener
    @Async
    public void publishOrderCompletedEvent(UUID orderId) {
        // Publish event để các service khác xử lý
        log.info("Order payment completed: {}", orderId);
        // ApplicationEventPublisher có thể được inject để publish events
    }

    // Scheduled task để cleanup expired payments
    @Scheduled(fixedRate = 300000) // Chạy mỗi 5 phút
    public void cleanupExpiredPayments() {
        List<Payment> expiredPayments = paymentRepository
                .findExpiredPendingPayments(new Date());

        for (Payment payment : expiredPayments) {
            payment.setPaymentStatus(PaymentStatus.EXPIRED);
            paymentRepository.save(payment);
            log.info("Marked payment as expired: {}", payment.getPaymentReference());
        }
    }

    public List<PaymentResponse> getPendingPayments() {
        List<Payment> pendingPayments = paymentRepository.findPendingQRPayments();
        return pendingPayments.stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }
}
