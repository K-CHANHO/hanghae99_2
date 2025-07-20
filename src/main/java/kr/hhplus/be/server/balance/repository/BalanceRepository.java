package kr.hhplus.be.server.balance.repository;

import kr.hhplus.be.server.balance.entity.Balance;

import java.util.Optional;

public interface BalanceRepository {

    Optional<Balance> findById(String userId);

}
