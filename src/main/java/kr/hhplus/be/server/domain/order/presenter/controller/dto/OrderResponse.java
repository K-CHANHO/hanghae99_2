package kr.hhplus.be.server.domain.order.presenter.controller.dto;

import kr.hhplus.be.server.domain.order.application.facade.dto.OrderProcessResult;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;
    private String userId;
    private int totalPrice;
    private String status;
    private Timestamp orderDate;

    public static OrderResponse from(OrderProcessResult orderProcessResult) {
        return OrderResponse.builder()
                .orderId(orderProcessResult.getOrderId())
                .userId(orderProcessResult.getUserId())
                .totalPrice(orderProcessResult.getTotalPrice())
                .status(orderProcessResult.getStatus())
                .orderDate(orderProcessResult.getOrderDate())
                .build();
    }

    public static OrderResponse from(CreateOrderResult createdOrder) {
        return OrderResponse.builder()
                .orderId(createdOrder.getOrderId())
                .userId(createdOrder.getUserId())
                .totalPrice(createdOrder.getTotalPrice())
                .status(createdOrder.getStatus())
                .orderDate(createdOrder.getCreatedAt())
                .build();
    }
}
