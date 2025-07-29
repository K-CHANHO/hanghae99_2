package kr.hhplus.be.server.domain.payment.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.domain.payment.application.service.dto.PayCommand;
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
    private String status; // PENDING, PAID
    private int paidPrice;
    private Timestamp paidAt;

    public void pay() {
        this.status = "PAID";
        this.paidAt = new Timestamp(System.currentTimeMillis());
    }

    public void create(PayCommand payCommand) {
        this.userId = payCommand.getUserId();
        this.orderId = payCommand.getOrderId();
        this.status = payCommand.getStatus();
        this.paidPrice = (int) (payCommand.getTotalPrice() * (1 - payCommand.getDiscountRate()));
    }
}
