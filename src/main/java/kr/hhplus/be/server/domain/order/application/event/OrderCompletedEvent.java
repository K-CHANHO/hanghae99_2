package kr.hhplus.be.server.domain.order.application.event;

import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCompletedEvent {
    private final Long orderId;
    private final int orderPrice;
    List<OrderProductDto> orderProductDtoList;

    public OrderCompletedEvent(Long orderId, int totalPrice, List<OrderProductDto> orderProductDtoList) {
        this.orderId = orderId;
        this.orderPrice = totalPrice;
        this.orderProductDtoList = orderProductDtoList;
    }
}
