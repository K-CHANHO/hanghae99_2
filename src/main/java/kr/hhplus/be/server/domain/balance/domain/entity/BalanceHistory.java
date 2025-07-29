package kr.hhplus.be.server.domain.balance.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
public class BalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long balanceHistoryId;
    private String userId;
    private int amount;
    private String type;
    private Timestamp createdAt;

    public BalanceHistory(String userId, int amount, String type) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}
