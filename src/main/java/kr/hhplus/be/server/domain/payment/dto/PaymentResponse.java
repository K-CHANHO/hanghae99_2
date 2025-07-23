package kr.hhplus.be.server.domain.payment.dto;

import lombok.Data;

@Data
public class PaymentResponse {

    private String paymentId;
    private String orderId; // 주문 ID
    private String userId; // 사용자 ID
    private String price;

}
