package kr.hhplus.be.server.domain.balance.infra;

import kr.hhplus.be.server.domain.balance.domain.entity.BalanceHistory;
import kr.hhplus.be.server.domain.balance.domain.repository.BalanceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BalanceHistoryRepositoryImpl implements BalanceHistoryRepository {

    private final BalanceHistoryJpaRepository jpaRepository;
    @Override
    public BalanceHistory save(BalanceHistory balanceHistory) {
        return jpaRepository.save(balanceHistory);
    }
}
