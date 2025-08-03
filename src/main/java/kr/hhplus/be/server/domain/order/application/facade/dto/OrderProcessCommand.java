package kr.hhplus.be.server.domain.order.application.facade.dto;

import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import kr.hhplus.be.server.domain.order.presenter.controller.dto.OrderRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class OrderProcessCommand {

    private String userId;
    private Long userCouponId;
    private List<OrderProductDto> orderProductDtoList;

    public static OrderProcessCommand from(OrderRequest orderRequest){
        return OrderProcessCommand.builder()
                .userId(orderRequest.getUserId())
                .userCouponId(orderRequest.getUserCouponId())
                .orderProductDtoList(orderRequest.getOrderProductDtoList())
                .build();
    }

}
