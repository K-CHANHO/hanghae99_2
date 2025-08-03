package kr.hhplus.be.server.domain.product.application.facade.dto;

import kr.hhplus.be.server.domain.product.application.service.dto.GetProductResult;
import kr.hhplus.be.server.domain.product.application.service.dto.GetProductsResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class GetTopProductsResult {

    List<TopProductDto> topProductDtoList;

    @Builder
    @Getter
    public static class TopProductDto {
        private Long productId;
        private String productName;
        private int price;

        public static TopProductDto from(GetProductResult getProductResult){
            return TopProductDto.builder()
                    .productId(getProductResult.getProductId())
                    .productName(getProductResult.getProductName())
                    .price(getProductResult.getPrice())
                    .build();
        }
    }


    public static GetTopProductsResult from(GetProductsResult getProductsResult) {
        List<TopProductDto> topProductDtos = getProductsResult.getProductResults().stream()
                .map(TopProductDto::from)
                .toList();

        return GetTopProductsResult.builder()
                .topProductDtoList(topProductDtos)
                .build();
    }
}
