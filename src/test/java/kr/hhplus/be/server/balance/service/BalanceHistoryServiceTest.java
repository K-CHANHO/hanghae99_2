package kr.hhplus.be.server.balance.service;

import kr.hhplus.be.server.domain.balance.entity.BalanceHistory;
import kr.hhplus.be.server.domain.balance.repository.BalanceHistoryRepository;
import kr.hhplus.be.server.domain.balance.service.BalanceHistoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BalanceHistoryServiceTest {

    @InjectMocks
    private BalanceHistoryService balanceHistoryService;

    @Mock
    private BalanceHistoryRepository balanceHistoryRepository;

    @ParameterizedTest
    @ValueSource(strings = {"CHARGE", "USE"})
    @DisplayName("잔액 이력 저장 서비스 테스트")
    public void saveChargeHistory(String type) {
        // given
        String userId = "sampleUserId";
        int chargeAmount = 10000;
        when(balanceHistoryRepository.save(any(BalanceHistory.class)))
                .thenReturn(new BalanceHistory(userId, chargeAmount, type));

        // when
        BalanceHistory balanceHistory = balanceHistoryService.saveBalanceHistory(userId, chargeAmount, type);

        // then
        assertThat(balanceHistory).isNotNull();
        assertThat(balanceHistory.getUserId()).isEqualTo(userId);
        assertThat(balanceHistory.getAmount()).isEqualTo(chargeAmount);
        assertThat(balanceHistory.getType()).isEqualTo(type);
    }
}
