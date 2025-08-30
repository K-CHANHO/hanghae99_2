package kr.hhplus.be.server.domain.payment.application.event;

import kr.hhplus.be.server.domain.coupon.application.event.CouponUsedEvent;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PaidEvent {

    private String userId;
    private Long orderId;
    private int totalPrice;
    private double discountRate;
    private List<OrderProductDto> orderProductDtoList;

    public PaidEvent(CouponUsedEvent event) {
        this.userId = event.getUserId();
        this.orderId = event.getOrderId();
        this.totalPrice = event.getTotalPrice();
        this.discountRate = event.getDiscountRate();
        this.orderProductDtoList = event.getOrderProductDtoList();
    }
}
