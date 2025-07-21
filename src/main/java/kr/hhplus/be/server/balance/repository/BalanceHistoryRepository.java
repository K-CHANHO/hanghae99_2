package kr.hhplus.be.server.balance.repository;

import kr.hhplus.be.server.balance.entity.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {
}
