package kr.hhplus.be.server.domain.balance.dto;


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

    public ViewBalanceResponse(ViewBalanceServiceResponse serviceResponse) {
        this.userId = serviceResponse.getUserId();
        this.balance = serviceResponse.getBalance();
    }

}
