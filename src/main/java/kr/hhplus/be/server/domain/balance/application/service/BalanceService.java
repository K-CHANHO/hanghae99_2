package kr.hhplus.be.server.domain.balance.application.service;

import kr.hhplus.be.server.common.aop.DistributedLock;
import kr.hhplus.be.server.domain.balance.application.service.dto.*;
import kr.hhplus.be.server.domain.balance.domain.entity.Balance;
import kr.hhplus.be.server.domain.balance.domain.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService{

    private final BalanceRepository balanceRepository;
    private final RedissonClient redissonClient;

    public GetBalanceResult getBalance(GetBalanceCommand getBalanceCommand){

        Balance balance = balanceRepository.findById(getBalanceCommand.getUserId())
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));
        return GetBalanceResult.from(balance);
    }

    @DistributedLock(key = "'balance:charge:' + #chargeBalanceCommand.userId")
    @Transactional
    public ChargeBalanceResult chargeBalance(ChargeBalanceCommand chargeBalanceCommand) {
        Balance balance = balanceRepository.findById(chargeBalanceCommand.getUserId())
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));
        balance.charge(chargeBalanceCommand.getAmount());
        Balance chargedBalance = balanceRepository.save(balance);
        return ChargeBalanceResult.from(chargedBalance);

    }

    @DistributedLock(key = "'balance:use:' + #useBalanceCommand.userId")
    @Transactional
    public UseBalanceResult useBalance(UseBalanceCommand useBalanceCommand) {
        Balance balance = balanceRepository.findById(useBalanceCommand.getUserId())
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));
        balance.use(useBalanceCommand.getUseAmount(), useBalanceCommand.getDiscountRate());
        Balance usedBalance = balanceRepository.save(balance);
        return UseBalanceResult.from(usedBalance);
    }
}
