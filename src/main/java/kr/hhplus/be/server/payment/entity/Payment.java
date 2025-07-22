package kr.hhplus.be.server.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Entity
@Getter
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    private String userId;
    private Long orderId;
    private String status;
    private int paidPrice;
    private Timestamp paidAt;

    public void pay() {
        this.status = "PAID";
        this.paidAt = new Timestamp(System.currentTimeMillis());
    }
}
