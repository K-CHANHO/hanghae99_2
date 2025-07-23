package kr.hhplus.be.server.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductStock {

    @Id
    private Long productId;
    private int stockQuantity;

    public void reduceStock(int orderQuantity) {
        if (this.getStockQuantity() < orderQuantity) {
            throw new RuntimeException("재고가 부족합니다.");
        }
        this.stockQuantity -= orderQuantity;
    }
}
