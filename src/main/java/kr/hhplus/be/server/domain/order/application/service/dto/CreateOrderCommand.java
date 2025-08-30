package kr.hhplus.be.server.domain.order.application.service.dto;

import kr.hhplus.be.server.domain.order.application.facade.dto.OrderProcessCommand;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import kr.hhplus.be.server.domain.order.presenter.controller.dto.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class CreateOrderCommand {

    private String userId;
    private List<OrderProductDto> orderProductDtoList;
    private Long userCouponId;

    public static CreateOrderCommand from(OrderProcessCommand orderProcessCommand) {
        return CreateOrderCommand.builder()
                .userId(orderProcessCommand.getUserId())
                .orderProductDtoList(orderProcessCommand.getOrderProductDtoList())
                .build();
    }

    public static CreateOrderCommand from(OrderRequest request) {
        return CreateOrderCommand.builder()
                .userId(request.getUserId())
                .orderProductDtoList(request.getOrderProductDtoList())
                .userCouponId(request.getUserCouponId())
                .build();
    }
}
