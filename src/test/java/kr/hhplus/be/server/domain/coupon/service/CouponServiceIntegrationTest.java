package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.application.service.CouponService;
import kr.hhplus.be.server.domain.coupon.application.service.dto.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.domain.repository.CouponStockRepository;
import kr.hhplus.be.server.domain.coupon.domain.repository.UserCouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

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

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @ParameterizedTest
    @ValueSource(ints = {1,99,100,101,200})
    @DisplayName("쿠폰발급_동시성 테스트")
    void coupon_concurrency_fail(int issueAmount) throws InterruptedException {
        // given
        int threadCount = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(issueAmount);

        // when
        AtomicInteger failCnt = new AtomicInteger();
        for(int i = 0; i< issueAmount; i++){
            int finalI = i;

            executorService.submit(() -> {
                try{
                    IssueCouponCommand issueCouponCommand = new IssueCouponCommand("sampleUserId"+ finalI, 5L);
                    couponService.issueCoupon(issueCouponCommand);
                } catch (Exception e){
                    if(e.getMessage().equals("쿠폰이 소진되었습니다.")) {
                        failCnt.getAndIncrement();
                    }
                } finally{
                    latch.countDown();
                }
            });
        }

        latch.await();

        Long couponStock = couponStockRepository.findById(5L).get().getQuantity();

        // then
        assertThat(couponStock).isEqualTo(Math.max(100-issueAmount, 0));
        assertThat(failCnt.get()).isEqualTo(Math.max(issueAmount-100, 0));

    }

    @Test
    @DisplayName("쿠폰발급_레디스")
    void coupon_issue_redis() throws InterruptedException {
        // given
        Long couponId = 6L;
        redisTemplate.opsForValue().set("coupon:stock:" + couponId, 40); // 쿠폰 재고 초기화

        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        AtomicInteger failCnt = new AtomicInteger();
        for(int i = 0; i< threadCount; i++){
            int finalI = i;

            executorService.submit(() -> {
                try{
                    IssueCouponCommand issueCouponCommand = new IssueCouponCommand("sampleUserId"+ finalI, couponId);
                    couponService.issueCouponRedis(issueCouponCommand);
                } catch (Exception e){
                    if(e.getMessage().equals("쿠폰이 소진되었습니다.")) {
                        failCnt.getAndIncrement();
                    }
                } finally{
                    latch.countDown();
                }
            });
        }

        latch.await();
        Integer couponStock = (Integer) redisTemplate.opsForValue().get("coupon:stock:" + couponId);

        // then
        assertThat(couponStock).isEqualTo(0L);
        assertThat(failCnt.get()).isEqualTo(10);
        assertThatNoException().isThrownBy(() ->
                userCouponRepository.findByUserIdAndCouponId("sampleUserId0", couponId).orElseThrow(() -> new RuntimeException("쿠폰 발급 실패"))
        );


    }
}
