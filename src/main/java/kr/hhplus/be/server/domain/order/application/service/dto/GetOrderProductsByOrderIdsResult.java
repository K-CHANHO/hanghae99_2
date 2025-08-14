package kr.hhplus.be.server.domain.order.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderProductsByOrderIdsResult {

    private List<Long> productIds;

    public static GetOrderProductsByOrderIdsResult from(List<Long> top5OrderProducts) {
        return GetOrderProductsByOrderIdsResult.builder()
                .productIds(top5OrderProducts)
                .build();
    }
}
