package kr.hhplus.be.server.domain.balance.infra;

import kr.hhplus.be.server.domain.balance.domain.entity.Balance;
import kr.hhplus.be.server.domain.balance.domain.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BalanceRepositoryImpl implements BalanceRepository {

    private final BalanceJpaRepository jpaRepository;


    @Override
    public Optional<Balance> findById(String userId) {
        return jpaRepository.findById(userId);
    }

    @Override
    public Optional<Balance> findByIdWithPessimisticLock(String userId) {
        return jpaRepository.findByIdWithPessimisticLock(userId);
    }

    @Override
    public Balance save(Balance balance) {
        return jpaRepository.save(balance);
    }
}
