package kr.hhplus.be.server.domain.payment.application.service.dto;

import kr.hhplus.be.server.domain.payment.domain.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class PayResult {

    private Long paymentId;
    private String userId;
    private Long orderId;
    private String status; // PENDING, PAID
    private int paidPrice;
    private Timestamp paidAt;

    public PayResult(Payment createdPayment) {
        this. paymentId = createdPayment.getPaymentId();
        this.userId = createdPayment.getUserId();
        this.orderId = createdPayment.getOrderId();
        this.status = createdPayment.getStatus();
        this.paidPrice = createdPayment.getPaidPrice();
        this.paidAt = createdPayment.getPaidAt();
    }
}
