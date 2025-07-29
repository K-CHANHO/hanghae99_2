package kr.hhplus.be.server.domain.balance.domain.repository;

import kr.hhplus.be.server.domain.balance.domain.entity.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {
}
