package kr.hhplus.be.server.balance.dto;

import lombok.Data;

@Data
public class ChargeBalanceRequest {
    private String userId;
    private int amount; // 충전할 금액
    private String transactionId; // 거래 ID (중복 방지용)
}
