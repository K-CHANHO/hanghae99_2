package kr.hhplus.be.server.balance.service;

import kr.hhplus.be.server.balance.entity.Balance;
import kr.hhplus.be.server.balance.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService{

    private final BalanceRepository balanceRepository;

    public Balance getBalance(String userId){

        return balanceRepository.findById(userId).orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));
    }

    public Balance chargeBalance(String userId, int chargeAmount) {
        Balance balance = balanceRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));

        balance.charge(chargeAmount);

        return balanceRepository.save(balance);
    }

    public Balance useBalance(String userId, int useAmount) {
        Balance balance = balanceRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));

        balance.use(useAmount);

        return balanceRepository.save(balance);

    }
}
