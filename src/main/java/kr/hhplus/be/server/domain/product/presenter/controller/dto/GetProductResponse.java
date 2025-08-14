package kr.hhplus.be.server.domain.product.presenter.controller.dto;

import kr.hhplus.be.server.domain.product.application.service.dto.GetProductResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GetProductResponse {
    private Long productId;
    private String productName;
    private int stock;
    private int price;

    public static GetProductResponse from(GetProductResult getProductResult) {
        return GetProductResponse.builder()
                .productId(getProductResult.getProductId())
                .productName(getProductResult.getProductName())
                .stock(getProductResult.getStock())
                .price(getProductResult.getPrice())
                .build();
    }
}
