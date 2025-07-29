package kr.hhplus.be.server.domain.balance.domain.repository;

import kr.hhplus.be.server.domain.balance.domain.entity.BalanceHistory;

public interface BalanceHistoryRepository {
    BalanceHistory save(BalanceHistory balanceHistory);
}
