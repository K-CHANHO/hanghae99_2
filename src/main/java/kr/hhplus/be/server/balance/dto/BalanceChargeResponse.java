package kr.hhplus.be.server.balance.dto;

import lombok.Data;

@Data
public class BalanceChargeResponse {

    private String userId; // 사용자 ID
    private int newBalance; // 업데이트된 잔액
}
