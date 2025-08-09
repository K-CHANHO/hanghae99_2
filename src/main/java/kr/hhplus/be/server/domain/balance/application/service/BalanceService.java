package kr.hhplus.be.server.domain.balance.application.service;

import kr.hhplus.be.server.domain.balance.application.service.dto.*;
import kr.hhplus.be.server.domain.balance.domain.entity.Balance;
import kr.hhplus.be.server.domain.balance.domain.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceService{

    private final BalanceRepository balanceRepository;

    public ViewBalanceResult getBalance(ViewBalanceCommand viewBalanceCommand){

        Balance balance = balanceRepository.findById(viewBalanceCommand.getUserId())
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));
        return ViewBalanceResult.from(balance);
    }

    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 200)
    )
    @Transactional
    public ChargeBalanceResult chargeBalance(ChargeBalanceCommand chargeBalanceCommand) {
        Balance balance = balanceRepository.findById(chargeBalanceCommand.getUserId())
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));

        balance.charge(chargeBalanceCommand.getAmount());

        Balance chargedBalance = balanceRepository.save(balance);
        return ChargeBalanceResult.from(chargedBalance);
    }

    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 200)
    )
    @Transactional
    public UseBalanceResult useBalance(UseBalanceCommand useBalanceCommand) {
        Balance balance = balanceRepository.findById(useBalanceCommand.getUserId())
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));

        balance.use(useBalanceCommand.getUseAmount(), useBalanceCommand.getDiscountRate());

        Balance usedBalance = balanceRepository.save(balance);

        return UseBalanceResult.from(usedBalance);

    }
}
