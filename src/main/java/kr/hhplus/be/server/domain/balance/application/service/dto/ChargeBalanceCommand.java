package kr.hhplus.be.server.domain.balance.application.service.dto;

import kr.hhplus.be.server.domain.balance.presenter.controller.dto.ChargeBalanceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChargeBalanceCommand {

    private String userId;
    private int amount;

    public static ChargeBalanceCommand from(ChargeBalanceRequest request) {
        return ChargeBalanceCommand.builder()
                .userId(request.getUserId())
                .amount(request.getAmount())
                .build();
    }
}
