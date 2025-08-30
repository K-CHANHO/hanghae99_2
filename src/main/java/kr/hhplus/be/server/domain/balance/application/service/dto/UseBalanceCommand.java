package kr.hhplus.be.server.domain.balance.application.service.dto;

import kr.hhplus.be.server.domain.coupon.application.service.dto.UseCouponResult;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderResult;
import kr.hhplus.be.server.domain.payment.application.event.PaidEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UseBalanceCommand {
    private String userId;
    private int useAmount;
    private double discountRate;

    public static UseBalanceCommand from(CreateOrderResult createOrderResult, UseCouponResult useCouponResult) {
        return UseBalanceCommand.builder()
                .userId(createOrderResult.getUserId())
                .useAmount(createOrderResult.getTotalPrice())
                .discountRate(useCouponResult.getDiscountRate())
                .build();
    }

    public static UseBalanceCommand from(PaidEvent event) {
        return UseBalanceCommand.builder()
                .userId(event.getUserId())
                .useAmount(event.getTotalPrice())
                .discountRate(event.getDiscountRate())
                .build();
    }
}
