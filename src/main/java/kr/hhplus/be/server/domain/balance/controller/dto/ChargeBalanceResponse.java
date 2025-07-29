package kr.hhplus.be.server.domain.balance.controller.dto;

import kr.hhplus.be.server.domain.balance.service.dto.ChargeBalanceResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChargeBalanceResponse {

    private String userId; // 사용자 ID
    private int newBalance; // 업데이트된 잔액

    public ChargeBalanceResponse(ChargeBalanceResult serviceResponse) {
        this.userId = serviceResponse.getUserId();
        this.newBalance = serviceResponse.getBalance();
    }
}
