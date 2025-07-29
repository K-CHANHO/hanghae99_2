package kr.hhplus.be.server.domain.balance.service.dto;

import kr.hhplus.be.server.domain.balance.controller.dto.ChargeBalanceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChargeBalanceCommand {

    private String userId;
    private int amount;

    public ChargeBalanceCommand(ChargeBalanceRequest request) {
        this.userId = request.getUserId();
        this.amount = request.getAmount();
    }
}
