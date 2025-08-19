package kr.hhplus.be.server.domain.coupon.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class CouponWorker {

    private final RedisTemplate<String, String> redisTemplate;

    @Scheduled(fixedDelay = 1000) // 1초마다 실행
    public void processCouponIssueQueue(){
        Set<ZSetOperations.TypedTuple<String>> user = redisTemplate.opsForZSet().rangeWithScores("coupon:queue:", 0, 0);
        if(user == null || user.isEmpty()) { break; }
    }
}
