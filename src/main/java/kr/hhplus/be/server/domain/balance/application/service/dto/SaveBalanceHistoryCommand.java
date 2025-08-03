package kr.hhplus.be.server.domain.balance.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SaveBalanceHistoryCommand {
    private String userId;
    private int amount;
    private String type;
}
