package kr.hhplus.be.server.domain.balance.application.service.dto;

import kr.hhplus.be.server.domain.balance.domain.entity.BalanceHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
@AllArgsConstructor
public class SaveBalanceHistoryResult {

    private Long balanceHistoryId;
    private String userId;
    private int amount;
    private String type;
    private Timestamp createdAt;

    public static SaveBalanceHistoryResult from(BalanceHistory savedHistory) {
        return SaveBalanceHistoryResult.builder()
                .balanceHistoryId(savedHistory.getBalanceHistoryId())
                .userId(savedHistory.getUserId())
                .amount(savedHistory.getAmount())
                .type(savedHistory.getType())
                .createdAt(savedHistory.getCreatedAt())
                .build();
    }
}
