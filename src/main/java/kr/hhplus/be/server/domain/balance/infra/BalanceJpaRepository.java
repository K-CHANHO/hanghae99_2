package kr.hhplus.be.server.domain.balance.infra;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.balance.domain.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BalanceJpaRepository extends JpaRepository<Balance, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Balance b WHERE b.userId = :userId")
    Optional<Balance> findByIdWithPessimisticLock(@Param("userId") String userId);
}
