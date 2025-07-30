package kr.hhplus.be.server.domain.balance.service;

import kr.hhplus.be.server.domain.balance.application.service.BalanceHistoryService;
import kr.hhplus.be.server.domain.balance.application.service.dto.SaveBalanceHistoryCommand;
import kr.hhplus.be.server.domain.balance.application.service.dto.SaveBalanceHistoryResult;
import kr.hhplus.be.server.domain.balance.domain.entity.BalanceHistory;
import kr.hhplus.be.server.domain.balance.domain.repository.BalanceHistoryRepository;
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
        SaveBalanceHistoryCommand saveBalanceHistoryCommand = SaveBalanceHistoryCommand.builder().userId(userId).amount(chargeAmount).type(type).build();
        when(balanceHistoryRepository.save(any(BalanceHistory.class)))
                .thenReturn(new BalanceHistory(userId, chargeAmount, type));

        // when
        SaveBalanceHistoryResult saveBalanceHistoryResult = balanceHistoryService.saveBalanceHistory(saveBalanceHistoryCommand);

        // then
        assertThat(saveBalanceHistoryResult).isNotNull();
        assertThat(saveBalanceHistoryResult.getUserId()).isEqualTo(userId);
        assertThat(saveBalanceHistoryResult.getAmount()).isEqualTo(chargeAmount);
        assertThat(saveBalanceHistoryResult.getType()).isEqualTo(type);
    }
}
