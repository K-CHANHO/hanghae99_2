package kr.hhplus.be.server.domain.balance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewBalanceServiceRequest {
    private String userId;

    public ViewBalanceServiceRequest(String userId) {
        this.userId = userId;
    }
}
