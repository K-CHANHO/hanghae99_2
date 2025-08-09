package kr.hhplus.be.server.domain.balance.presenter.controller.dto;


import kr.hhplus.be.server.domain.balance.application.service.dto.GetBalanceResult;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetBalanceResponse {

    private String userId;
    private int balance;

    public static GetBalanceResponse from(GetBalanceResult getBalanceResult) {
        return GetBalanceResponse.builder()
                .userId(getBalanceResult.getUserId())
                .balance(getBalanceResult.getBalance())
                .build();
    }
}
