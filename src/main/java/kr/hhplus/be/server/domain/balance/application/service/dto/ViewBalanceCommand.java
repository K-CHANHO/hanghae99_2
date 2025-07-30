package kr.hhplus.be.server.domain.balance.application.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewBalanceCommand {
    private String userId;

    public static ViewBalanceCommand from(String userId) {
        return ViewBalanceCommand.builder()
                .userId(userId)
                .build();
    }
}
