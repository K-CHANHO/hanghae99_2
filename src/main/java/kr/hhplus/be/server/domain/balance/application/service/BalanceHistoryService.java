package kr.hhplus.be.server.domain.balance.application.service;

import kr.hhplus.be.server.domain.balance.domain.entity.BalanceHistory;
import kr.hhplus.be.server.domain.balance.domain.repository.BalanceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceHistoryService {

    private final BalanceHistoryRepository balanceHistoryRepository;

    public BalanceHistory saveBalanceHistory(String userId, int amount, String type) {
        BalanceHistory balanceHistory = new BalanceHistory(userId, amount, type);
        return balanceHistoryRepository.save(balanceHistory);
    }

}
