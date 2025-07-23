package kr.hhplus.be.server.domain.order.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;
    @ManyToOne
    private Order order;
    private Long productId;
    private int quantity;
    private int price;

    public int getTotalPrice(){
        return this.getQuantity() * this.getPrice();
    }
}
