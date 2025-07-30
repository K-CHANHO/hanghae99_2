package kr.hhplus.be.server.domain.balance.application.service;

import kr.hhplus.be.server.domain.balance.application.service.dto.SaveBalanceHistoryCommand;
import kr.hhplus.be.server.domain.balance.application.service.dto.SaveBalanceHistoryResult;
import kr.hhplus.be.server.domain.balance.domain.entity.BalanceHistory;
import kr.hhplus.be.server.domain.balance.domain.repository.BalanceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class BalanceHistoryService {

    private final BalanceHistoryRepository balanceHistoryRepository;

    public SaveBalanceHistoryResult saveBalanceHistory(SaveBalanceHistoryCommand saveBalanceHistoryCommand) {

        BalanceHistory balanceHistory = BalanceHistory.builder()
                .userId(saveBalanceHistoryCommand.getUserId())
                .amount(saveBalanceHistoryCommand.getAmount())
                .type(saveBalanceHistoryCommand.getType())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        BalanceHistory savedHistory = balanceHistoryRepository.save(balanceHistory);
        return SaveBalanceHistoryResult.from(savedHistory);
    }

}
