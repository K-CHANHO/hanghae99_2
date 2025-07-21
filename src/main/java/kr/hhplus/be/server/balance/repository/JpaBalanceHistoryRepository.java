package kr.hhplus.be.server.balance.repository;

import kr.hhplus.be.server.balance.entity.BalanceHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaBalanceHistoryRepository implements BalanceHistoryRepository{

    private final SpringDataBalanceHistoryRepository jpaBalanceHistoryRepository;

    @Override
    public BalanceHistory save(BalanceHistory balanceHistory) {
        return jpaBalanceHistoryRepository.save(balanceHistory);
    }
}
