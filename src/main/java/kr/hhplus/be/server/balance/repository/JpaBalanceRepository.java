package kr.hhplus.be.server.balance.repository;

import kr.hhplus.be.server.balance.entity.Balance;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBalanceRepository implements BalanceRepository {

    private final JpaRepository<Balance, String> jpaRepository;


    @Override
    public Optional<Balance> findById(String userId) {
        return jpaRepository.findById(userId);
    }
}
