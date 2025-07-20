package kr.hhplus.be.server.balance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Balance {

    @Id
    private String userId;

    @Column
    private int balance;

    public Balance(String userId, int balance) {
        if(balance < 0){
            throw new IllegalArgumentException("잔액은 0원 이상이어야 합니다.");
        }
        this.userId = userId;
        this.balance = balance;
    }

    public void charge(int chargeAmount) {
        if(chargeAmount < 0){
            throw new RuntimeException("충전은 양수 값만 가능합니다.");
        }
        this.balance += chargeAmount;
    }
}
