package kr.hhplus.be.server.domain.balance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewBalanceServiceRequestDto {
    private String userId;

    public ViewBalanceServiceRequestDto(String userId) {
        this.userId = userId;
    }
}
