package kr.hhplus.be.server.domain.balance.presenter.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetBalanceRequest {
    private String userId;
}
