package kr.hhplus.be.server.domain.balance.presenter.controller.dto;

import kr.hhplus.be.server.domain.balance.application.service.dto.ChargeBalanceResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChargeBalanceResponse {

    private String userId; // 사용자 ID
    private int newBalance; // 업데이트된 잔액

    public static ChargeBalanceResponse from(ChargeBalanceResult chargeBalanceResult) {
        return ChargeBalanceResponse.builder()
                .userId(chargeBalanceResult.getUserId())
                .newBalance(chargeBalanceResult.getBalance())
                .build();
    }
}
