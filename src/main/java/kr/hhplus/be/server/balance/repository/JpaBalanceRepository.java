package kr.hhplus.be.server.balance.repository;

import kr.hhplus.be.server.balance.entity.Balance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBalanceRepository implements BalanceRepository {

    private final SpringDataBalanceRepository jpaRepository;

    @Override
    public Optional<Balance> findById(String userId) {
        return jpaRepository.findById(userId);
    }

    @Override
    public Balance save(Balance updatedBalance) {
        return jpaRepository.save(updatedBalance);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }


}
