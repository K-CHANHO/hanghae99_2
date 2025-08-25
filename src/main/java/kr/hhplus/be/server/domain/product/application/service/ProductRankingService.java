package kr.hhplus.be.server.domain.product.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRankingService {
    private final RedisTemplate<String, String> redisTemplate;

    private static final String PRODUCT_RANK_KEY_PREFIX = "product:ranking:";

    public void increaseSales(Long productId, int quantity){
        String key = PRODUCT_RANK_KEY_PREFIX + LocalDate.now();
        redisTemplate.opsForZSet().incrementScore(key, productId.toString(), quantity);
        redisTemplate.expire(key, Duration.ofDays(4));
    }

    public List<Long> getTopProductIdsLastNDays(int days, int limit) {
        LocalDate today = LocalDate.now();
        List<String> keys = new ArrayList<>();

        for (int i = 0; i < days; i++) {
            keys.add(PRODUCT_RANK_KEY_PREFIX + (today.minusDays(i)));
        }

        String rankingKey = "product:ranking:last" + days + "days";

        // ZUNIONSTORE 실행 (합산)
        redisTemplate.opsForZSet()
                .unionAndStore(keys.get(0), keys.subList(1, keys.size()), rankingKey);

        Set<ZSetOperations.TypedTuple<String>> zSet = redisTemplate.opsForZSet()
                .reverseRangeWithScores(rankingKey, 0, limit - 1);

        List<Long> topProductIds = new ArrayList<>();
        if(zSet != null){
            zSet.stream().forEach(tuple ->
                    topProductIds.add(Long.parseLong(tuple.getValue()))
            );
        }

        return topProductIds;
    }

}
