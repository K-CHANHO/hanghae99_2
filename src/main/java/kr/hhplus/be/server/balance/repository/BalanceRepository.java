package kr.hhplus.be.server.balance.repository;

import kr.hhplus.be.server.balance.entity.Balance;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository {

    Optional<Balance> findById(String userId);

    Balance save(Balance updatedBalance);

    void deleteAll();
}
