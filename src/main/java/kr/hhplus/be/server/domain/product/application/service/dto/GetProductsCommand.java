package kr.hhplus.be.server.domain.product.application.service.dto;

import kr.hhplus.be.server.domain.order.application.service.dto.GetOrderProductsByOrderIdsResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class GetProductsCommand {
    private List<Long> productIds;

    public static GetProductsCommand from(GetOrderProductsByOrderIdsResult orderProductsByOrderIds) {
        return GetProductsCommand.builder()
                .productIds(orderProductsByOrderIds.getProductIds())
                .build();
    }
}
