package kr.hhplus.be.server.domain.balance.presenter.controller.dto;


import kr.hhplus.be.server.domain.balance.application.service.dto.ViewBalanceResult;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewBalanceResponse {

    private String userId;
    private int balance;

    public static ViewBalanceResponse from(ViewBalanceResult viewBalanceResult) {
        return ViewBalanceResponse.builder()
                .userId(viewBalanceResult.getUserId())
                .balance(viewBalanceResult.getBalance())
                .build();
    }
}
