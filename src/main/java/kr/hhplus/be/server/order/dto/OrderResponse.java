package kr.hhplus.be.server.order.dto;

import lombok.Data;

@Data
public class OrderResponse {

    private String orderId;
    private String userId;
    private int totalPrice;
    private String status;
    private String orderDate;

}
