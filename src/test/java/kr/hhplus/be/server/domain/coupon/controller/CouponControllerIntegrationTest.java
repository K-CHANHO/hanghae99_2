package kr.hhplus.be.server.domain.coupon.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {
        "/testSql/cleanup.sql",
        "/testSql/balance.sql",
        "/testSql/product.sql",
        "/testSql/coupon.sql"
})
@Slf4j
class CouponControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    @DisplayName("쿠폰 발급 API 테스트_이미 발급받은 쿠폰일 경우")
    void issueCouponWithAlreadyIssued() throws Exception {
        // given
        Long couponId = 1L;
        String userId = "sampleUserId";
        String url = "/api/v1/coupon/issue";
        String requestBody = "{ \"userId\": \"" + userId + "\", \"couponId\": "+ couponId +" }";
        String userKey = "coupon:issue:" + couponId + ":userSet"; // 중복 확인 키
        redisTemplate.opsForSet().add(userKey, userId);
        redisTemplate.opsForValue().set("coupon:stock:" + couponId, 50); // 쿠폰 재고 초기화

        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("이미 발급된 쿠폰입니다."))
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    @DisplayName("쿠폰 발급 API 테스트_쿠폰이 소진된 경우")
    void issueCouponWithExhausted() throws Exception {
        // given
        Long couponId = 2L;
        String userId = "sampleUserId1";
        String url = "/api/v1/coupon/issue";
        String requestBody = "{ \"userId\": \"" + userId + "\", \"couponId\": "+ couponId +" }";
        redisTemplate.opsForValue().set("coupon:stock:" + couponId, 0);

        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("쿠폰이 소진되었습니다."))
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    @DisplayName("쿠폰 발급 API 테스트_성공")
    void issueCoupon() throws Exception {
        // given
        Long couponId = 6L;
        String userId = "sampleUserId";
        String url = "/api/v1/coupon/issue";
        String requestBody = "{ \"userId\": \"" + userId + "\", \"couponId\": "+ couponId +" }";
        redisTemplate.opsForValue().set("coupon:stock:" + couponId, 40);

        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("쿠폰 발급 요청 성공"))
                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.data.couponId").value(couponId))
//                .andExpect(jsonPath("$.data.couponName").value("깜짝 20% 할인쿠폰"))
                .andExpect(jsonPath("$.data").doesNotExist())  // 데이터가 없음을 확인
        ;

    }

    @Test
    @DisplayName("쿠폰 발급 API 동시요청")
    void issueCouponWithConcurrency() throws Exception {
        // given
        int threadCount = 10000; // 동시에 보낼 요청 수
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long couponId = 6L;
        String url = "/api/v1/coupon/issue";
        redisTemplate.opsForValue().set("coupon:stock:" + couponId, 10000);

        long start = System.currentTimeMillis();
        // when
        for(int i=0; i<threadCount; i++){
            executorService.submit(() ->{
                String userId = "sampleUserId" + threadCount;
                String requestBody = "{ \"userId\": \"" + userId + "\", \"couponId\": "+ couponId +" }";
                try{
                    mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    ).andExpect(status().isOk());
                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        long end = System.currentTimeMillis();

        // then
        log.info("소요시간 : " + (end - start) + "ms");
    }
}