package kr.hhplus.be.server.domain.product.presenter.controller.dto;

import kr.hhplus.be.server.domain.product.application.service.dto.GetProductResult;
import kr.hhplus.be.server.domain.product.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ViewProductResponse {
    private Long productId;
    private String productName;
    private int stock;
    private int price;

    public static ViewProductResponse from(GetProductResult getProductResult) {
        return ViewProductResponse.builder()
                .productId(getProductResult.getProductId())
                .productName(getProductResult.getProductName())
                .stock(getProductResult.getStock())
                .price(getProductResult.getPrice())
                .build();
    }
}
