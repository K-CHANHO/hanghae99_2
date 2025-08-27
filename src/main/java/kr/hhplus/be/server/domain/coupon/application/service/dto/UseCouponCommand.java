package kr.hhplus.be.server.domain.coupon.application.service.dto;

import kr.hhplus.be.server.domain.order.application.event.OrderCreatedEvent;
import kr.hhplus.be.server.domain.order.application.facade.dto.OrderProcessCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UseCouponCommand {
    private String userId;
    private Long couponId;

    public static UseCouponCommand from(OrderProcessCommand orderProcessCommand) {
        return UseCouponCommand.builder()
                .userId(orderProcessCommand.getUserId())
                .couponId(orderProcessCommand.getUserCouponId())
                .build();
    }

    public static UseCouponCommand from(OrderCreatedEvent event) {
        return UseCouponCommand.builder()
                .userId(event.getUserId())
                .couponId(event.getUserCouponId())
                .build();
    }
}
