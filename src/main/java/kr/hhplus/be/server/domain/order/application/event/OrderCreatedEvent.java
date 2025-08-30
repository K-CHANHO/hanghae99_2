package kr.hhplus.be.server.domain.order.application.event;

import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderResult;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreatedEvent {

    private Long orderId;
    private String userId;
    private List<OrderProductDto> orderProductDtoList;
    private Long userCouponId;
    private int totalPrice;


    public OrderCreatedEvent(Long orderId, String userId, List<OrderProductDto> orderProductDtoList, Long userCouponId) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderProductDtoList = orderProductDtoList;
        this.userCouponId = userCouponId;
    }

    public OrderCreatedEvent(CreateOrderCommand createOrderCommand, CreateOrderResult createOrderResult) {
        this.orderId = createOrderResult.getOrderId();
        this.userId = createOrderResult.getUserId();
        this.orderProductDtoList = createOrderCommand.getOrderProductDtoList();
        this.userCouponId = createOrderCommand.getUserCouponId();
        this.totalPrice = createOrderResult.getTotalPrice();
    }
}
