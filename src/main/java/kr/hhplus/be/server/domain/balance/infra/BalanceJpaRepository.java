package kr.hhplus.be.server.domain.balance.infra;

import kr.hhplus.be.server.domain.balance.domain.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceJpaRepository extends JpaRepository<Balance, String> {
}
