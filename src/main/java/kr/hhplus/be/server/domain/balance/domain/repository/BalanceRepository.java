package kr.hhplus.be.server.domain.balance.domain.repository;

import kr.hhplus.be.server.domain.balance.domain.entity.Balance;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository {

    Optional<Balance> findById(String userId);
    Optional<Balance> findByIdWithPessimisticLock(String userId);

    Balance save(Balance balance);
}
