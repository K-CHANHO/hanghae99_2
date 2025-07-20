package kr.hhplus.be.server.balance.service;

import kr.hhplus.be.server.balance.entity.Balance;
import kr.hhplus.be.server.balance.repository.BalanceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        String userId = "sampleUserId";
        Mockito.when(balanceRepository.findById(userId)).thenReturn(Optional.of(new Balance(userId, 100000)));

        // when
        Balance balance = balanceService.getBalance(userId);

        // then
        assertThat(balance).isNotNull();
        assertThat(userId).isEqualTo(balance.getUserId());
        assertThat(100000).isEqualTo(balance.getBalance());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -100, -50000})
    @DisplayName("잔액 충전 서비스 테스트 - 음수 값을 입력한 경우")
    public void chargeBalanceWithNegativeValue(int chargeAmount){
        // given
        String userId = "sampleUserId";
        Mockito.when(balanceRepository.findById(userId)).thenReturn(Optional.of(new Balance(userId, 100000)));

        // when, then
        assertThatThrownBy(() -> balanceService.chargeBalance(userId, chargeAmount))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("충전은 양수 값만 가능합니다.");

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10000, 50000})
    @DisplayName("잔액 충전 서비스 테스트")
    public void chargeBalance(int chargeAmount) {
        // given
        String userId = "sampleUserId";
        int initialBalance = 100000;

        Mockito.when(balanceRepository.findById(userId)).thenReturn(Optional.of(new Balance(userId, initialBalance)));
        Mockito.when(balanceRepository.save(Mockito.any(Balance.class))).thenReturn(
                new Balance(userId, initialBalance + chargeAmount)
        );

        // when
        Balance updatedBalance = balanceService.chargeBalance(userId, chargeAmount);

        // then
        assertThat(updatedBalance).isNotNull();
        assertThat(updatedBalance.getBalance()).isEqualTo(initialBalance + chargeAmount);
    }
}
