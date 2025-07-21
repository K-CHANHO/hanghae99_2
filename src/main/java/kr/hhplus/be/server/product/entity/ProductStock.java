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
    private String productId;
    private int stockQuantity;

    public void reduceStock(int orderQuantity) {
        this.stockQuantity -= orderQuantity;
    }
}
