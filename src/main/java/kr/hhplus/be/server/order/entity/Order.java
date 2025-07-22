package kr.hhplus.be.server.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private String userId;
    private String status;
    private int totalPrice;
    private Timestamp createdAt;
    private List<OrderProduct> orderProductList;

    public void setStatus(String status) {
        this.status = status;
    }
}
