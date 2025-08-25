package kr.hhplus.be.server.domain.balance.presenter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargeBalanceRequest {
    private String userId;
    private Integer amount; // 충전할 금액
    private String transactionId; // 거래 ID (중복 방지용)
}
