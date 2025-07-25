package kr.hhplus.be.server.domain.balance.dto;


import kr.hhplus.be.server.domain.balance.entity.Balance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewBalanceResponse {

    private String userId;
    private int balance;

    public void from(Balance balance){
        this.userId = balance.getUserId();
        this.balance = balance.getBalance();
    }

}
