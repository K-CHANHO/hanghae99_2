package kr.hhplus.be.server.balance.entity;

import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class Balance {

    private String userId;
    private int balance;

    public Balance(String userId, int balance) {
        if(balance < 0){
            throw new IllegalArgumentException("잔액은 0원 이상이어야 합니다.");
        }
        this.userId = userId;
        this.balance = balance;
    }
}
