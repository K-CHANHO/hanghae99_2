package kr.hhplus.be.server.domain.balance.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Builder
public class Balance {

    @Id
    private String userId;
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

    public void use(int useAmount, double discountRate) {
        if(useAmount < 0){
            throw new RuntimeException("사용은 양수 값만 가능합니다.");
        } else if(this.balance < useAmount){
            throw new RuntimeException("잔고가 부족합니다.");
        }
        this.balance -= (int) (useAmount * (1-discountRate));
    }
}
