package kr.hhplus.be.server.domain.payment.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PayCommand {
    private String userId;
    private Long orderId;
    private String status;
    private int totalPrice;
    private double discountRate;
}
