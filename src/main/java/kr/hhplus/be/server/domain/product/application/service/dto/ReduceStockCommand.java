package kr.hhplus.be.server.domain.product.application.service.dto;

import kr.hhplus.be.server.domain.order.application.service.dto.OrderProductSaveResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ReduceStockCommand {
    private Long productId;
    private int orderQuantity;

    public static ReduceStockCommand from(OrderProductSaveResult.OrderProductDto2 product) {
        return ReduceStockCommand.builder()
                .productId(product.getProductId())
                .orderQuantity(product.getQuantity())
                .build();
    }
}
