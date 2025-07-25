package kr.hhplus.be.server.domain.product.dto;

import kr.hhplus.be.server.domain.product.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ViewTopProductResponse {

    private List<productDto> topProducts;

    @Data
    @Builder
    public static class productDto {
        private Long productId;
        private String productName;
        private int price;
        private int stock;
    }

    public void from(List<Product> topProducts) {
        this.topProducts = topProducts.stream().map(product -> productDto.builder()
                            .productId(product.getProductId())
                            .productName(product.getProductName())
                            .price(product.getPrice())
                            .stock(product.getProductStock().getStockQuantity())
                            .build())
                            .toList();
    }

}
