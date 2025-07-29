package kr.hhplus.be.server.domain.order.application.facade.dto;

import kr.hhplus.be.server.domain.order.application.service.dto.ChangeStatusResult;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
public class OrderProcessResult {

    private Long orderId;
    private String userId;
    private int totalPrice;
    private String status;
    private Timestamp orderDate;

    public static OrderProcessResult from(ChangeStatusResult order){
        return OrderProcessResult.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .orderDate(order.getCreatedAt())
                .build();
    }
}
