package kr.hhplus.be.server.domain.order.application.service.dto;

import kr.hhplus.be.server.domain.order.domain.entity.Order;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
public class CreateOrderResult {

    private Long orderId;
    private String userId;
    private String status; // PENDING, PAID
    private int totalPrice;
    private Timestamp createdAt;

    public static CreateOrderResult from(Order savedOrder) {
        return CreateOrderResult.builder()
                .orderId(savedOrder.getOrderId())
                .userId(savedOrder.getUserId())
                .status(savedOrder.getStatus())
                .totalPrice(savedOrder.getTotalPrice())
                .createdAt(savedOrder.getCreatedAt())
                .build();
    }
}
