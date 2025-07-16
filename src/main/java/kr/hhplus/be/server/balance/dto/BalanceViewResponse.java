package kr.hhplus.be.server.balance.dto;


import lombok.Data;

@Data
public class BalanceViewResponse {

    private String userId;
    private int balance;

}
