package kr.hhplus.be.server.domain.order.application.service.dto;

import kr.hhplus.be.server.domain.order.application.facade.dto.OrderProcessCommand;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class OrderProductSaveCommand {
    private String userId;
    private Long orderId;
    private List<OrderProductDto> orderProductDtoList;

    public static OrderProductSaveCommand from(OrderProcessCommand orderProcessCommand, CreateOrderResult createOrderResult) {
        return OrderProductSaveCommand.builder()
                .userId(orderProcessCommand.getUserId())
                .orderId(createOrderResult.getOrderId())
                .orderProductDtoList(orderProcessCommand.getOrderProductDtoList())
                .build();
    }
}
