package kr.hhplus.be.server.balance.repository;

import kr.hhplus.be.server.balance.entity.BalanceHistory;

public interface BalanceHistoryRepository {
    BalanceHistory save(BalanceHistory balanceHistory);
}
