package kr.hhplus.be.server.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    @OneToOne
    @JoinColumn(name = "productStock_productId")
    private ProductStock productStock;
    private int price;

    public void reduceStock(int orderQuantity) {
        if (productStock.getStockQuantity() < orderQuantity) {
            throw new RuntimeException("재고가 부족합니다.");
        }
        this.productStock.reduceStock(orderQuantity);
    }
}
