package kr.hhplus.be.server.domain.balance.application.service;

import kr.hhplus.be.server.domain.balance.application.service.dto.ChargeBalanceCommand;
import kr.hhplus.be.server.domain.balance.application.service.dto.ChargeBalanceResult;
import kr.hhplus.be.server.domain.balance.application.service.dto.ViewBalanceCommand;
import kr.hhplus.be.server.domain.balance.application.service.dto.ViewBalanceResult;
import kr.hhplus.be.server.domain.balance.domain.entity.Balance;
import kr.hhplus.be.server.domain.balance.domain.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService{

    private final BalanceRepository balanceRepository;

    public ViewBalanceResult getBalance(ViewBalanceCommand viewBalanceCommand){

        Balance balance = balanceRepository.findById(viewBalanceCommand.getUserId())
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));
        return new ViewBalanceResult(balance);
    }

    public ChargeBalanceResult chargeBalance(ChargeBalanceCommand serviceRequest) {
        Balance balance = balanceRepository.findById(serviceRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));

        balance.charge(serviceRequest.getAmount());

        Balance chargedBalance = balanceRepository.save(balance);
        return new ChargeBalanceResult(chargedBalance);
    }

    public Balance useBalance(String userId, int useAmount, double discountRate) {
        Balance balance = balanceRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));

        balance.use(useAmount, discountRate);

        return balanceRepository.save(balance);

    }
}
