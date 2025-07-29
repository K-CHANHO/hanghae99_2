package kr.hhplus.be.server.domain.balance.service;

import kr.hhplus.be.server.domain.balance.dto.ViewBalanceServiceRequest;
import kr.hhplus.be.server.domain.balance.dto.ViewBalanceServiceResponse;
import kr.hhplus.be.server.domain.balance.entity.Balance;
import kr.hhplus.be.server.domain.balance.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService{

    private final BalanceRepository balanceRepository;

    public ViewBalanceServiceResponse getBalance(ViewBalanceServiceRequest viewBalanceServiceRequest){

        Balance balance = balanceRepository.findById(viewBalanceServiceRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));
        return new ViewBalanceServiceResponse(balance);
    }

    public Balance chargeBalance(String userId, int chargeAmount) {
        Balance balance = balanceRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));

        balance.charge(chargeAmount);

        return balanceRepository.save(balance);
    }

    public Balance useBalance(String userId, int useAmount, double discountRate) {
        Balance balance = balanceRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));

        balance.use(useAmount, discountRate);

        return balanceRepository.save(balance);

    }
}
