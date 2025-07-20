package kr.hhplus.be.server.balance.service;

import kr.hhplus.be.server.balance.dto.ViewBalanceRequest;
import kr.hhplus.be.server.balance.entity.Balance;
import kr.hhplus.be.server.balance.repository.BalanceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {

    @InjectMocks
    private BalanceService balanceService;

    @Spy
    private BalanceRepository balanceRepository;

    @Test
    @DisplayName("잔액 조회 서비스 테스트")
    public void getBalance(){
        // given
        ViewBalanceRequest viewBalanceRequest = ViewBalanceRequest.builder()
                        .userId("sampleUserId")
                        .build();
        Mockito.when(balanceRepository.findById(viewBalanceRequest.getUserId())).thenReturn(Optional.of(new Balance(viewBalanceRequest.getUserId(), 100000)));

        // when
        Balance balance = balanceService.getBalance(viewBalanceRequest);

        // then
        Assertions.assertNotNull(balance);
        Assertions.assertEquals(viewBalanceRequest.getUserId(), balance.getUserId());
        Assertions.assertEquals(100000, balance.getBalance());
    }
}
