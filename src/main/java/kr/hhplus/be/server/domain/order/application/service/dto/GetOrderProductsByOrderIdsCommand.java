package kr.hhplus.be.server.domain.order.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class GetOrderProductsByOrderIdsCommand {

    private List<Long> orderIds;

    public static GetOrderProductsByOrderIdsCommand from(List<Long> orderIds) {
        return GetOrderProductsByOrderIdsCommand.builder()
                .orderIds(orderIds)
                .build();
    }
}
