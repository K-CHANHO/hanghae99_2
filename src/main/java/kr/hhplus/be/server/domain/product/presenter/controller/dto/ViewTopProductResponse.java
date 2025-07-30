package kr.hhplus.be.server.domain.product.presenter.controller.dto;

import kr.hhplus.be.server.domain.product.application.facade.dto.GetTopProductsResult;
import kr.hhplus.be.server.domain.product.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ViewTopProductResponse {

    private List<topProductDto> topProducts;

    @Builder
    @Getter
    public static class topProductDto {
        private Long productId;
        private String productName;
        private int price;
    }

    public static ViewTopProductResponse from(GetTopProductsResult topProductsResult) {
        List<topProductDto> list = topProductsResult.getTopProductDtoList().stream().map(product -> topProductDto.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .price(product.getPrice())
                        .build())
                .toList();

        return ViewTopProductResponse.builder()
                .topProducts(list)
                .build();
    }

}
