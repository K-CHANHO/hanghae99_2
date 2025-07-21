package kr.hhplus.be.server.balance.service;

import kr.hhplus.be.server.balance.entity.Balance;

public interface BalanceService {
    Balance getBalance(String userId);
    Balance chargeBalance(String userId, int chargeAmount) ;
    Balance useBalance(String userId, int useAmount);
}
