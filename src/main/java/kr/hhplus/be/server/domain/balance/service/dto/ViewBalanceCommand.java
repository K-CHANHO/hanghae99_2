package kr.hhplus.be.server.domain.balance.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewBalanceCommand {
    private String userId;

    public ViewBalanceCommand(String userId) {
        this.userId = userId;
    }
}
