package kr.hhplus.be.server.balance.dto;


import kr.hhplus.be.server.balance.entity.Balance;
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

    public static ViewBalanceResponse toDto(Balance balance){
        return ViewBalanceResponse.builder()
                .userId(balance.getUserId())
                .balance(balance.getBalance())
                .build();
    }

}
