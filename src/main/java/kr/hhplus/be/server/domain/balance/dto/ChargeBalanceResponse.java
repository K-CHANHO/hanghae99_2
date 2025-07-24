package kr.hhplus.be.server.domain.balance.dto;

import kr.hhplus.be.server.domain.balance.entity.Balance;
import lombok.Data;

@Data
public class ChargeBalanceResponse {

    private String userId; // 사용자 ID
    private int newBalance; // 업데이트된 잔액

    public void from(Balance balance) {
        this.userId = balance.getUserId();
        this.newBalance = balance.getBalance();
    }
}
