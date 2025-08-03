package kr.hhplus.be.server.domain.product.application.service.dto;

import kr.hhplus.be.server.domain.product.domain.entity.ProductStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ReduceStockResult {
    private Long productId;
    private int stockQuantity;

    public static ReduceStockResult from(ProductStock reducedStock) {
        return ReduceStockResult.builder()
                .productId(reducedStock.getProductId())
                .stockQuantity(reducedStock.getStockQuantity())
                .build();
    }
}
