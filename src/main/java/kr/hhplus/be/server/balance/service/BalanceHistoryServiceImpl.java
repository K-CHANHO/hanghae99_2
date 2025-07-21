package kr.hhplus.be.server.balance.service;

import kr.hhplus.be.server.balance.entity.BalanceHistory;
import kr.hhplus.be.server.balance.repository.BalanceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceHistoryServiceImpl implements BalanceHistoryService {

    private final BalanceHistoryRepository balanceHistoryRepository;

    @Override
    public BalanceHistory saveBalanceHistory(String userId, int amount, String type) {
        BalanceHistory balanceHistory = new BalanceHistory(userId, amount, type);
        return balanceHistoryRepository.save(balanceHistory);
    }

}
