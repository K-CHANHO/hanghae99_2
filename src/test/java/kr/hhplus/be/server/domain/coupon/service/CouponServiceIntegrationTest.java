package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.application.service.CouponService;
import kr.hhplus.be.server.domain.coupon.application.service.dto.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.domain.repository.CouponStockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = {
        "/testSql/cleanup.sql",
        "/testSql/coupon.sql"
})
public class CouponServiceIntegrationTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponStockRepository couponStockRepository;

    @Test
    @DisplayName("쿠폰발급_동시성 테스트")
    void coupon_concurrency_fail() throws InterruptedException {
        // given
        int threadCount = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        AtomicInteger failCnt = new AtomicInteger();
        for(int i = 0; i< threadCount; i++){
            int finalI = i;

            executorService.submit(() -> {
                try{
                    IssueCouponCommand issueCouponCommand = new IssueCouponCommand("sampleUserId"+ finalI, 5L);
                    couponService.issueCoupon(issueCouponCommand);
                } catch (Exception e){
                    failCnt.getAndIncrement();
                } finally{
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        assertThat(couponStockRepository.findById(5L).get().getQuantity()).isEqualTo(0);
        assertThat(failCnt.get()).isEqualTo(100);

    }
}
