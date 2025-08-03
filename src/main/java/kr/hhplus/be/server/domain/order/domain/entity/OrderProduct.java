package kr.hhplus.be.server.domain.order.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;
    private Long productId;
    private int quantity;
    private int price;

    private Long orderId; // order의 pk

    public int getTotalPrice(){
        return this.getQuantity() * this.getPrice();
    }
}
