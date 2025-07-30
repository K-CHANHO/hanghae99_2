package kr.hhplus.be.server.domain.payment.application.service.dto;

import kr.hhplus.be.server.domain.coupon.application.service.dto.UseCouponResult;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PayCommand {
    private String userId;
    private Long orderId;
    private int totalPrice;
    private double discountRate;

    public static PayCommand from(CreateOrderResult createOrderResult, UseCouponResult useCouponResult) {
        return PayCommand.builder()
                .userId(createOrderResult.getUserId())
                .orderId(createOrderResult.getOrderId())
                .totalPrice(createOrderResult.getTotalPrice())
                .discountRate(useCouponResult.getDiscountRate())
                .build();
    }
}
