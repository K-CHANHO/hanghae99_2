package kr.hhplus.be.server.domain.balance.service;

import kr.hhplus.be.server.domain.balance.application.service.BalanceService;
import kr.hhplus.be.server.domain.balance.application.service.dto.ChargeBalanceCommand;
import kr.hhplus.be.server.domain.balance.application.service.dto.UseBalanceCommand;
import kr.hhplus.be.server.domain.balance.application.service.dto.GetBalanceCommand;
import kr.hhplus.be.server.domain.balance.application.service.dto.GetBalanceResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = {
        "/testSql/cleanup.sql",
        "/testSql/balance.sql"
})
public class BalanceServiceIntegrationTest {
    @Autowired
    private BalanceService balanceService;

    @Test
    @DisplayName("잔액 충전 동시성 테스트")
    public void chargeBalanceWithConcurrency() throws InterruptedException {
        // given
        int threadCount = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        ChargeBalanceCommand chargeBalanceCommand = ChargeBalanceCommand.builder()
                .userId("sampleUserId9").amount(10000)
                .build();
        GetBalanceCommand getBalanceCommand = GetBalanceCommand.builder()
                .userId("sampleUserId9").build();

        // when
        AtomicInteger failCnt = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
               try{
                   balanceService.chargeBalance(chargeBalanceCommand);
               } catch (OptimisticLockingFailureException e){
                   failCnt.getAndIncrement();
               } catch (RuntimeException e){
                   failCnt.getAndIncrement();
               } finally{
                   latch.countDown();
               }
            });
        }
        latch.await();

        GetBalanceResult balance = balanceService.getBalance(getBalanceCommand);

        // then
        assertThat(balance.getBalance()).isEqualTo(10000 * (200-failCnt.get()));
    }

    @Test
    @DisplayName("잔액 사용 동시성 테스트")
    public void useBalanceWithConcurrency() throws InterruptedException {
        // given
        int threadCount = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        UseBalanceCommand useBalanceCommand = UseBalanceCommand.builder()
                .userId("sampleUserId8").useAmount(10000)
                .build();
        GetBalanceCommand getBalanceCommand = GetBalanceCommand.builder()
                .userId("sampleUserId8").build();

        // when
        AtomicInteger failCnt = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try{
                    balanceService.useBalance(useBalanceCommand);
                } catch (OptimisticLockingFailureException e){
                    failCnt.getAndIncrement();
                } catch (RuntimeException e){
                    failCnt.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        GetBalanceResult balance = balanceService.getBalance(getBalanceCommand);

        // then
        assertThat(balance.getBalance()).isEqualTo(4000000 - 10000 * (200-failCnt.get()));

    }
}
