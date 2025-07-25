package kr.hhplus.be.server.domain.order.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Builder
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;
    private Long orderId;
    private Long productId;
    private int quantity;
    private int price;

    @Transient @Setter
    private Order order;

    public int getTotalPrice(){
        return this.getQuantity() * this.getPrice();
    }
}
