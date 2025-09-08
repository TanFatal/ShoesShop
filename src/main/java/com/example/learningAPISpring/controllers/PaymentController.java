package com.example.learningAPISpring.controllers;


import com.example.learningAPISpring.dto.Request.ConfirmPaymentRequest;
import com.example.learningAPISpring.dto.Request.QRPaymentRequest;
import com.example.learningAPISpring.dto.Response.ApiResponse;
import com.example.learningAPISpring.dto.Response.PaymentResponse;
import com.example.learningAPISpring.exception.InvalidPaymentStateException;
import com.example.learningAPISpring.exception.OrderNotFoundException;
import com.example.learningAPISpring.exception.PaymentExpiredException;
import com.example.learningAPISpring.exception.PaymentNotFoundException;
import com.example.learningAPISpring.service.PaymentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/payments")
@Slf4j
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/qr/create")
    public ResponseEntity<ApiResponse<PaymentResponse>> createQRPayment(
            @Valid @RequestBody QRPaymentRequest request) {

        try {
            PaymentResponse response = paymentService.createQRPayment(request);
            return ResponseEntity.ok(
                    new ApiResponse<>("success", "QR Payment created successfully", response)
            );
        } catch (OrderNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error creating QR payment", e);
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("error", e.getMessage(), null)
            );
        }
    }

    @GetMapping("/status/{paymentReference}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentStatus(
            @PathVariable String paymentReference) {

        try {
            PaymentResponse response = paymentService.getPaymentStatus(paymentReference);
            return ResponseEntity.ok(
                    new ApiResponse<>("success", "Payment status retrieved", response)
            );
        } catch (PaymentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error getting payment status", e);
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("error", e.getMessage(), null)
            );
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByOrder(
            @PathVariable UUID orderId) {

        try {
            PaymentResponse response = paymentService.getPaymentByOrderId(orderId);
            return ResponseEntity.ok(
                    new ApiResponse<>("success", "Payment found", response)
            );
        } catch (PaymentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error getting payment by order", e);
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("error", e.getMessage(), null)
            );
        }
    }

    @PostMapping("/confirm/{paymentReference}")
    public ResponseEntity<ApiResponse<PaymentResponse>> confirmPayment(
            @PathVariable String paymentReference,
            @RequestBody ConfirmPaymentRequest request) {

        try {
            PaymentResponse response = paymentService.confirmPayment(
                    paymentReference,
                    request.getTransactionId()
            );
            return ResponseEntity.ok(
                    new ApiResponse<>("success", "Payment confirmed successfully", response)
            );
        } catch (PaymentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidPaymentStateException | PaymentExpiredException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("error", e.getMessage(), null)
            );
        } catch (Exception e) {
            log.error("Error confirming payment", e);
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("error", "Internal server error", null)
            );
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPendingPayments() {
        try {
            List<PaymentResponse> payments = paymentService.getPendingPayments();
            return ResponseEntity.ok(
                    new ApiResponse<>("success", "Pending payments retrieved", payments)
            );
        } catch (Exception e) {
            log.error("Error getting pending payments", e);
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("error", e.getMessage(), null)
            );
        }
    }
}
