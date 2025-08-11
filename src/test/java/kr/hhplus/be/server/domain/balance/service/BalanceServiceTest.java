package kr.hhplus.be.server.domain.balance.service;

import kr.hhplus.be.server.domain.balance.application.service.BalanceService;
import kr.hhplus.be.server.domain.balance.application.service.dto.*;
import kr.hhplus.be.server.domain.balance.domain.entity.Balance;
import kr.hhplus.be.server.domain.balance.domain.repository.BalanceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {

    @InjectMocks
    private BalanceService balanceService;

    @Mock
    private BalanceRepository balanceRepository;
    @Mock
    private RedissonClient redissonClient;
    @Mock
    private PlatformTransactionManager transactionManager;

    @Test
    @DisplayName("잔액 조회 서비스 테스트")
    public void getBalance(){
        // given
        String userId = "sampleUserId";
        GetBalanceCommand getBalanceCommand = GetBalanceCommand.from(userId);
        when(balanceRepository.findById(userId)).thenReturn(Optional.of(new Balance(userId, 100000)));

        // when
        GetBalanceResult serviceResponseDto = balanceService.getBalance(getBalanceCommand);

        // then
        assertThat(serviceResponseDto).isNotNull();
        assertThat(userId).isEqualTo(serviceResponseDto.getUserId());
        assertThat(100000).isEqualTo(serviceResponseDto.getBalance());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -100, -50000})
    @DisplayName("잔액 충전 서비스 테스트 - 음수 값을 충전한 경우")
    public void chargeBalanceWithNegativeValue(int chargeAmount) throws InterruptedException {
        // given
        String userId = "sampleUserId";
        ChargeBalanceCommand serviceRequest = ChargeBalanceCommand.builder().userId(userId).amount(chargeAmount).build();
        when(balanceRepository.findById(userId)).thenReturn(Optional.of(new Balance(userId, 100000)));

        RLock mockRLock = Mockito.mock(RLock.class);
        when(redissonClient.getLock(Mockito.anyString())).thenReturn(mockRLock);
        when(mockRLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(transactionManager.getTransaction(any())).thenReturn(any());

        // when, then
        assertThatThrownBy(() -> balanceService.chargeBalance(serviceRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("충전은 양수 값만 가능합니다.");

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10000, 50000})
    @DisplayName("잔액 충전 서비스 테스트")
    public void chargeBalance(int chargeAmount) throws InterruptedException {
        // given
        String userId = "sampleUserId";
        int initialBalance = 100000;
        ChargeBalanceCommand serviceRequest = ChargeBalanceCommand.builder().userId(userId).amount(chargeAmount).build();

        when(balanceRepository.findById(userId)).thenReturn(Optional.of(new Balance(userId, initialBalance)));
        when(balanceRepository.save(Mockito.any(Balance.class))).thenReturn(
                new Balance(userId, initialBalance + chargeAmount)
        );

        RLock mockRLock = Mockito.mock(RLock.class);
        when(redissonClient.getLock(Mockito.anyString())).thenReturn(mockRLock);
        when(mockRLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(transactionManager.getTransaction(any())).thenReturn(any());

        // when
        ChargeBalanceResult serviceResponse = balanceService.chargeBalance(serviceRequest);

        // then
        assertThat(serviceResponse).isNotNull();
        assertThat(serviceResponse.getBalance()).isEqualTo(initialBalance + chargeAmount);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10000, -50000})
    @DisplayName("잔액 사용 서비스 테스트 - 음수 값을 사용한 경우")
    public void useBalanceWithNegativeValue(int useAmount) throws InterruptedException {
        // given
        String userId = "sampleUserId";
        when(balanceRepository.findById(userId)).thenReturn(Optional.of(new Balance(userId, 100000)));
        UseBalanceCommand balanceCommand = UseBalanceCommand.builder().userId(userId).useAmount(useAmount).discountRate(0.1).build();

        RLock mockRLock = Mockito.mock(RLock.class);
        when(redissonClient.getLock(Mockito.anyString())).thenReturn(mockRLock);
        when(mockRLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(transactionManager.getTransaction(any())).thenReturn(any());

        // when, then
        assertThatThrownBy(() -> balanceService.useBalance(balanceCommand))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("사용은 양수 값만 가능합니다.");

    }

    @ParameterizedTest
    @ValueSource(ints = {100001, 150000, 999999})
    @DisplayName("잔액 사용 서비스 테스트 - 잔액보다 큰 금액 사용한 경우")
    public void useBalanceOver(int useAmount) throws InterruptedException {
        // given
        String userId = "sampleUserId";
        when(balanceRepository.findById(userId)).thenReturn(Optional.of(new Balance(userId, 100000)));
        UseBalanceCommand balanceCommand = UseBalanceCommand.builder().userId(userId).useAmount(useAmount).discountRate(0.1).build();

        RLock mockRLock = Mockito.mock(RLock.class);
        when(redissonClient.getLock(Mockito.anyString())).thenReturn(mockRLock);
        when(mockRLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(transactionManager.getTransaction(any())).thenReturn(any());

        // when, then
        assertThatThrownBy(() -> balanceService.useBalance(balanceCommand))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("잔고가 부족합니다.");

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 50000, 99999, 100000})
    @DisplayName("잔액 사용 서비스 테스트 - 정상")
    public void useBalance(int useAmount) throws InterruptedException {
        // given
        String userId = "sampleUserId";
        int initialBalance = 100000;
        UseBalanceCommand balanceCommand = UseBalanceCommand.builder().userId(userId).useAmount(useAmount).discountRate(0.1).build();

        when(balanceRepository.findById(userId)).thenReturn(Optional.of(new Balance(userId, initialBalance)));
        when(balanceRepository.save(Mockito.any(Balance.class))).thenReturn(
                new Balance(userId, initialBalance - useAmount)
        );

        RLock mockRLock = Mockito.mock(RLock.class);
        when(redissonClient.getLock(Mockito.anyString())).thenReturn(mockRLock);
        when(mockRLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(transactionManager.getTransaction(any())).thenReturn(any());

        // when
        UseBalanceResult useBalanceResult = balanceService.useBalance(balanceCommand);

        // then
        assertThat(useBalanceResult).isNotNull();
        assertThat(useBalanceResult.getUserId()).isEqualTo(userId);
        assertThat(useBalanceResult.getBalance()).isEqualTo(100000 - useAmount);
    }
}
