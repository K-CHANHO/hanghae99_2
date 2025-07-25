package kr.hhplus.be.server.domain.balance.repository;

import kr.hhplus.be.server.domain.balance.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, String> {

}
