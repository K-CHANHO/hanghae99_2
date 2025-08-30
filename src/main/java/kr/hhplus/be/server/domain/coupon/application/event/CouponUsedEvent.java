package kr.hhplus.be.server.domain.coupon.application.event;

import kr.hhplus.be.server.domain.coupon.application.service.dto.UseCouponResult;
import kr.hhplus.be.server.domain.order.application.event.OrderCreatedEvent;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import lombok.Getter;

import java.util.List;

@Getter
public class CouponUsedEvent {
    private String userId;
    private Long orderId;
    private int totalPrice;
    private double discountRate;
    private List<OrderProductDto> orderProductDtoList;


    public CouponUsedEvent(OrderCreatedEvent event, UseCouponResult useCouponResult) {
        this.userId = event.getUserId();
        this.orderId = event.getOrderId();
        this.totalPrice = event.getTotalPrice();
        this.discountRate = useCouponResult.getDiscountRate();
        this.orderProductDtoList = event.getOrderProductDtoList();
    }
}
