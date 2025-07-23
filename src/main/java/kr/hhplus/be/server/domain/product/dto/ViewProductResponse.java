package kr.hhplus.be.server.domain.product.dto;

import lombok.Data;

@Data
public class ViewProductResponse {
    private String productId;
    private String productName;
    private int stock;
    private int price;
}
