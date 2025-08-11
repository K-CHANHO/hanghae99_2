package kr.hhplus.be.server.domain.balance.application.service;

import kr.hhplus.be.server.domain.balance.application.service.dto.*;
import kr.hhplus.be.server.domain.balance.domain.entity.Balance;
import kr.hhplus.be.server.domain.balance.domain.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BalanceService{

    private final BalanceRepository balanceRepository;
    private final RedissonClient redissonClient;
    private final PlatformTransactionManager transactionManager;

    public GetBalanceResult getBalance(GetBalanceCommand getBalanceCommand){

        Balance balance = balanceRepository.findById(getBalanceCommand.getUserId())
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));
        return GetBalanceResult.from(balance);
    }

    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 200)
    )
    public ChargeBalanceResult chargeBalance(ChargeBalanceCommand chargeBalanceCommand) {
        RLock lock = redissonClient.getLock("lock:chargeBalance:" + chargeBalanceCommand.getUserId());

        try{
            if(lock.tryLock(10, 5, TimeUnit.SECONDS)) {
                TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
                try {
                    Balance balance = balanceRepository.findById(chargeBalanceCommand.getUserId())
                            .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));
                    balance.charge(chargeBalanceCommand.getAmount());
                    Balance chargedBalance = balanceRepository.save(balance);
                    transactionManager.commit(status);
                    return ChargeBalanceResult.from(chargedBalance);
                } catch (Exception e) {
                    transactionManager.rollback(status);
                    throw e;
                }
            } else {
                throw new RuntimeException("락을 획득하지 못하였습니다.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException();
        } finally {
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }

    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 200)
    )
    @Transactional
    public UseBalanceResult useBalance(UseBalanceCommand useBalanceCommand) {
        RLock lock = redissonClient.getLock("lock:chargeBalance:" + useBalanceCommand.getUserId());

        try{
            if(lock.tryLock(5,10, TimeUnit.SECONDS)){
                TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
                try {
                    Balance balance = balanceRepository.findById(useBalanceCommand.getUserId())
                            .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));
                    balance.use(useBalanceCommand.getUseAmount(), useBalanceCommand.getDiscountRate());
                    Balance usedBalance = balanceRepository.save(balance);
                    return UseBalanceResult.from(usedBalance);
                } catch (Exception e){
                    transactionManager.rollback(status);
                    throw e;
                }
            } else {
                throw new RuntimeException("락을 획득하지 못하였습니다.");
            }
        } catch (InterruptedException e){
            throw new RuntimeException();
        } finally {
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }

    }
}
