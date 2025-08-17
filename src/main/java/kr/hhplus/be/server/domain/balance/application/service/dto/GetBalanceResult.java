package kr.hhplus.be.server.domain.balance.application.service.dto;

import kr.hhplus.be.server.domain.balance.domain.entity.Balance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetBalanceResult {

    private String userId;
    private int balance;

    public static GetBalanceResult from(Balance balance) {
        return GetBalanceResult.builder()
                .userId(balance.getUserId())
                .balance(balance.getBalance())
                .build();
    }
}
