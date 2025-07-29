package kr.hhplus.be.server.domain.balance.controller.dto;


import kr.hhplus.be.server.domain.balance.service.dto.ViewBalanceResult;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewBalanceResponse {

    private String userId;
    private int balance;

    public ViewBalanceResponse(ViewBalanceResult serviceResponse) {
        this.userId = serviceResponse.getUserId();
        this.balance = serviceResponse.getBalance();
    }

}
