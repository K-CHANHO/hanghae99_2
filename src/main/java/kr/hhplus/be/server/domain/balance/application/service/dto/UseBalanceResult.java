package kr.hhplus.be.server.domain.balance.application.service.dto;

import kr.hhplus.be.server.domain.balance.domain.entity.Balance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UseBalanceResult {
    private String userId;
    private int balance;

    public static UseBalanceResult from(Balance usedBalance) {
        return UseBalanceResult.builder()
                .userId(usedBalance.getUserId())
                .balance(usedBalance.getBalance())
                .build();
    }
}
