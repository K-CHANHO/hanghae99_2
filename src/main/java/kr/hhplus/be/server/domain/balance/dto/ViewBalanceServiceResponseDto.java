package kr.hhplus.be.server.domain.balance.dto;

import kr.hhplus.be.server.domain.balance.entity.Balance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ViewBalanceServiceResponseDto {

    private String userId;
    private int balance;

    public ViewBalanceServiceResponseDto(Balance balance){
        this.userId = balance.getUserId();
        this.balance = balance.getBalance();
    }
}
