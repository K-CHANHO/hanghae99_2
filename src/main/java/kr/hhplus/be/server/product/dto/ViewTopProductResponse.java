package kr.hhplus.be.server.product.dto;

import lombok.Data;

import java.util.List;

@Data
public class ViewTopProductResponse {

    private List<Product> topProducts;

    @Data
    public static class Product {
        private String productId;
        private String productName;
        private int soldCount;
        private int price;
    }
}
