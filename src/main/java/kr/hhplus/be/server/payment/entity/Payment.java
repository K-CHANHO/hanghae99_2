package kr.hhplus.be.server.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    private String userId;
    private Long orderId;
    private String status;
    private int paidPrice;
    private Timestamp paidAt;

    public void pay(int totalPrice, double discountRate) {
        this.status = "PAID";
        this.paidPrice = (int) (totalPrice * (1 - discountRate));
        this.paidAt = new Timestamp(System.currentTimeMillis());
    }

    public void create(String userId, Long orderId, int totalPrice, double discountRate) {
        this.userId = userId;
        this.orderId = orderId;
        this.status = "PENDING";
        this.paidPrice = (int) (totalPrice * (1 - discountRate));
    }
}
