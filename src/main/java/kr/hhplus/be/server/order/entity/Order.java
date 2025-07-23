package kr.hhplus.be.server.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "`order`")
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

    public void changeStatus(String status) {
        this.status = status;
    }
}
