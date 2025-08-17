package kr.hhplus.be.server.domain.balance.application.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetBalanceCommand {
    private String userId;

    public static GetBalanceCommand from(String userId) {
        return GetBalanceCommand.builder()
                .userId(userId)
                .build();
    }
}
