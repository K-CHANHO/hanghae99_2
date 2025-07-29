package kr.hhplus.be.server.domain.order.application.service.dto;

import kr.hhplus.be.server.domain.order.domain.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
@AllArgsConstructor
public class ChangeStatusResult {

    private Long orderId;
    private String userId;
    private String status; // PENDING, PAID
    private int totalPrice;
    private Timestamp createdAt;

    public static ChangeStatusResult from(Order changedOrder) {
        return ChangeStatusResult.builder()
                .orderId(changedOrder.getOrderId())
                .userId(changedOrder.getUserId())
                .status(changedOrder.getStatus())
                .totalPrice(changedOrder.getTotalPrice())
                .createdAt(changedOrder.getCreatedAt())
                .build();
    }
}
