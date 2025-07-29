package kr.hhplus.be.server.domain.balance.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChargeBalanceRequest {
    private String userId;
    private int amount; // 충전할 금액
    private String transactionId; // 거래 ID (중복 방지용)
}
