package kr.hhplus.be.server.domain.order.dto;

import kr.hhplus.be.server.domain.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;
    private String userId;
    private int totalPrice;
    private String status;
    private Timestamp orderDate;

    public void from(Order order) {
        this.orderId = order.getOrderId();
        this.userId = order.getUserId();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
        this.orderDate = order.getCreatedAt();

    }
}
