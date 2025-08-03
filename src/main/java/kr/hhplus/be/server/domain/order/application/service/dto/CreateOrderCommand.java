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
public class CreateOrderCommand {

    private String userId;
    private List<OrderProductDto> orderProductDtoList;

    public static CreateOrderCommand from(OrderProcessCommand orderProcessCommand) {
        return CreateOrderCommand.builder()
                .userId(orderProcessCommand.getUserId())
                .orderProductDtoList(orderProcessCommand.getOrderProductDtoList())
                .build();
    }
}
