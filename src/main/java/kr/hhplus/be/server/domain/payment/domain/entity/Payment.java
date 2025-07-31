package kr.hhplus.be.server.domain.payment.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.payment.application.service.dto.PayCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(indexes = {
        @Index(name = "idx_payment_status_paid_at", columnList = "status, paid_at")
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @Column(length = 20)
    private String userId;
    @Column(length = 10)
    private String status; // PENDING, PAID
    private int paidPrice;
    private Timestamp paidAt;

    private Long orderId; // order의 pk

    public void pay() {
        this.status = "PAID";
        this.paidAt = new Timestamp(System.currentTimeMillis());
    }

    public void create(PayCommand payCommand) {
        this.userId = payCommand.getUserId();
        this.orderId = payCommand.getOrderId();
        this.status = "PENDING";
        this.paidPrice = (int) (payCommand.getTotalPrice() * (1 - payCommand.getDiscountRate()));
    }
}
