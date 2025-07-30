package kr.hhplus.be.server.domain.product.application.service.dto;

import kr.hhplus.be.server.domain.product.domain.entity.Product;
import kr.hhplus.be.server.domain.product.domain.entity.ProductStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class GetProductResult {
    private Long productId;
    private String productName;
    private int price;
    private int stock;

    public static GetProductResult from(Product product, ProductStock productStock) {
        return GetProductResult.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .stock(productStock.getStockQuantity())
                .build();
    }

}
