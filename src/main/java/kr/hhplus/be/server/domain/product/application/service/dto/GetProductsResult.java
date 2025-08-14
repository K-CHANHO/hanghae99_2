package kr.hhplus.be.server.domain.product.application.service.dto;

import kr.hhplus.be.server.domain.product.domain.entity.Product;
import kr.hhplus.be.server.domain.product.domain.entity.ProductStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetProductsResult {
    List<GetProductResult> productResults;

    public static GetProductsResult from(List<Product> products) {
        List<GetProductResult> collect = products.stream()
                .map(product -> GetProductResult.from(product, new ProductStock()))
                .toList();

        return GetProductsResult.builder()
                .productResults(collect)
                .build();
    }
}
