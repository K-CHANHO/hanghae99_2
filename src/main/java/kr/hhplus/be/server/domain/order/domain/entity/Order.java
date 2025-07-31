package kr.hhplus.be.server.domain.order.domain.entity;

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
    @Column(length = 20)
    private String userId;
    @Column(length = 10)
    private String status; // PENDING, PAID
    private int totalPrice;
    private Timestamp createdAt;

    public void changeStatus(String status) {
        this.status = status;
    }
}
