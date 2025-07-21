package kr.hhplus.be.server.order.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private String productId;
    private int quantity;
    private int price;

    public int getTotalPrice(){
        return this.getQuantity() * this.getPrice();
    }
}
