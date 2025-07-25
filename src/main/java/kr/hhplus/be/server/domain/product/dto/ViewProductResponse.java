package kr.hhplus.be.server.domain.product.dto;

import kr.hhplus.be.server.domain.product.entity.Product;
import lombok.Data;

@Data
public class ViewProductResponse {
    private Long productId;
    private String productName;
    private int stock;
    private int price;

    public void from(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.stock = product.getProductStock().getStockQuantity();
    }
}
