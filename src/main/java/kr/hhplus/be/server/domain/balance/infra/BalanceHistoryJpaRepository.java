package kr.hhplus.be.server.domain.balance.infra;

import kr.hhplus.be.server.domain.balance.domain.entity.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceHistoryJpaRepository extends JpaRepository<BalanceHistory, Long> {
}
