package kr.hhplus.be.server.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisHealthChecker {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis 서버 Ping 테스트
     * @return true = 서버 정상, false = 서버 응답 없음
     */
    public boolean isRedisAlive() {
        try {
            String pong = redisTemplate.execute(RedisConnectionCommands::ping);
            return "PONG".equals(pong);
        } catch (Exception e) {
            return false;
        }
    }
}
