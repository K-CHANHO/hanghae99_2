package kr.hhplus.be.server.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String productId;
    private String productName;
    @OneToOne
    @JoinColumn(name = "productStock_productId")
    private ProductStock productStock;

}
