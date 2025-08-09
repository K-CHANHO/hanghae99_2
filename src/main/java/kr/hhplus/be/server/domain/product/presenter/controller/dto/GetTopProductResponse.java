package kr.hhplus.be.server.domain.product.presenter.controller.dto;

import kr.hhplus.be.server.domain.product.application.facade.dto.GetTopProductsResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class GetTopProductResponse {

    private List<topProductDto> topProducts;

    @Builder
    @Getter
    public static class topProductDto {
        private Long productId;
        private String productName;
        private int price;
    }

    public static GetTopProductResponse from(GetTopProductsResult topProductsResult) {
        List<topProductDto> list = topProductsResult.getTopProductDtoList().stream().map(product -> topProductDto.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .price(product.getPrice())
                        .build())
                .toList();

        return GetTopProductResponse.builder()
                .topProducts(list)
                .build();
    }

}
