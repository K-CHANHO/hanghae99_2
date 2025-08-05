package kr.hhplus.be.server.domain.balance.application.service;

import kr.hhplus.be.server.domain.balance.application.service.dto.*;
import kr.hhplus.be.server.domain.balance.domain.entity.Balance;
import kr.hhplus.be.server.domain.balance.domain.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public ChargeBalanceResult chargeBalance(ChargeBalanceCommand chargeBalanceCommand) {
        Balance balance = balanceRepository.findByIdWithPessimisticLock(chargeBalanceCommand.getUserId())
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));

        balance.charge(chargeBalanceCommand.getAmount());

        Balance chargedBalance = balanceRepository.save(balance);
        return ChargeBalanceResult.from(chargedBalance);
    }

    @Transactional
    public UseBalanceResult useBalance(UseBalanceCommand useBalanceCommand) {
        Balance balance = balanceRepository.findByIdWithPessimisticLock(useBalanceCommand.getUserId())
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));

        balance.use(useBalanceCommand.getUseAmount(), useBalanceCommand.getDiscountRate());

        Balance usedBalance = balanceRepository.save(balance);

        return UseBalanceResult.from(usedBalance);

    }
}
