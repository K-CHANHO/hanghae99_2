package kr.hhplus.be.server.domain.balance.application.service.dto;

import kr.hhplus.be.server.domain.balance.domain.entity.Balance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChargeBalanceResult {
    private String userId;
    private int balance;

    public static ChargeBalanceResult from(Balance chargedBalance) {
        return ChargeBalanceResult.builder()
                .userId(chargedBalance.getUserId())
                .balance(chargedBalance.getBalance())
                .build();
    }
}
