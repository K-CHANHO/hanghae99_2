package kr.hhplus.be.server.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    private String userId;
    private String userCouponId;
    private List<OrderProductDto> orderProductDtoList;

}
