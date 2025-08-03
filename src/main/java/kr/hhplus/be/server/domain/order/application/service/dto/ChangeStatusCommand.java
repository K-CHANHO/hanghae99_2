package kr.hhplus.be.server.domain.order.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChangeStatusCommand {
    private Long orderId;
    private String status;

    public static ChangeStatusCommand from(CreateOrderResult createOrderResult) {
        return ChangeStatusCommand.builder()
                .orderId(createOrderResult.getOrderId())
                .status("PAID")
                .build();
    }
}
