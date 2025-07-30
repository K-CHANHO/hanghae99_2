package kr.hhplus.be.server.domain.balance.application.service.dto;

import kr.hhplus.be.server.domain.balance.domain.entity.Balance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ViewBalanceResult {

    private String userId;
    private int balance;

    public static ViewBalanceResult from(Balance balance) {
        return ViewBalanceResult.builder()
                .userId(balance.getUserId())
                .balance(balance.getBalance())
                .build();
    }
}
