package kr.hhplus.be.server.domain.payment.dto;

import lombok.Data;

@Data
public class PaymentRequest {

    private String orderId;
    private String userId;
    private String transactionId;

}
