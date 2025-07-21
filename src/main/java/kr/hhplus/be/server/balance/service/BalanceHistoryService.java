package kr.hhplus.be.server.balance.service;

import kr.hhplus.be.server.balance.entity.BalanceHistory;

public interface BalanceHistoryService {

    BalanceHistory saveBalanceHistory(String userId, int amount, String type);
}
