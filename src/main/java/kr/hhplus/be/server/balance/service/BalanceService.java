package kr.hhplus.be.server.balance.service;

import kr.hhplus.be.server.balance.dto.ViewBalanceRequest;
import kr.hhplus.be.server.balance.entity.Balance;
import kr.hhplus.be.server.balance.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public Balance getBalance(ViewBalanceRequest viewBalanceRequest){

        Balance balance = balanceRepository.findById(viewBalanceRequest.getUserId()).orElseThrow(() -> new RuntimeException("잔액을 조회할 수 없습니다."));

        return balance;
    }
}
