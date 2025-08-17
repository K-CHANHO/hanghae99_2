package kr.hhplus.be.server.domain.product.facade;

import kr.hhplus.be.server.domain.order.application.service.dto.GetOrderProductsByOrderIdsResult;
import kr.hhplus.be.server.domain.product.application.facade.ProductFacade;
import kr.hhplus.be.server.domain.product.application.facade.dto.GetTopProductsResult;
import kr.hhplus.be.server.domain.product.application.service.dto.GetProductResult;
import kr.hhplus.be.server.domain.product.application.service.dto.GetProductsResult;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@Sql(scripts = {
        "/testSql/cleanup.sql",
        "/testSql/order.sql",
        "/testSql/orderProduct.sql",
        "/testSql/payment.sql",
        "/testSql/product.sql"
})
@Slf4j
public class ProductFacadeIntegrationTest {
    @Autowired
    private ProductFacade productFacade;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("상위 상품 조회 테스트")
    public void getTopProducts(){
        // given


        // when
        GetTopProductsResult topProducts = productFacade.getTopProducts();

        // then
        Assertions.assertThat(topProducts.getTopProductDtoList()).isNotEmpty();
        Assertions.assertThat(topProducts.getTopProductDtoList()).hasSizeLessThanOrEqualTo(5);
    }

    @Test
    @DisplayName("레디스 연결 확인 테스트")
    void testRedisConnection() {
        String testKey = "redis_test_key";
        String testValue = "ok";

        // 값 세팅
        redisTemplate.opsForValue().set(testKey, testValue);

        // 값 가져오기
        Object value = redisTemplate.opsForValue().get(testKey);
        log.info("Redis returned: {}", value);

        Assertions.assertThat(testValue).isEqualTo(value);
    }

    @Test
    @DisplayName("상위 상품 조회 캐시 데이터 확인 테스트")
    public void getTopProductsInCache(){
        // given
        String topOrderIdsKey = "topOrderIds::top::orderIds";
        String topProductIdsKey = "topProductIds::top::productIds";
        String topProductsKey = "topProducts::top::products";

        // when
        log.info("첫번째 호출 시작 - DB 조회");
        long start = System.currentTimeMillis();
        GetTopProductsResult topProducts = productFacade.getTopProducts();
        long end = System.currentTimeMillis();
        log.info("첫번째 호출 끝");
        log.info("DB 조회 소요시간 : {}", (end - start) + "ms");

        log.info("두번째 호출 시작 - 캐시 조회");
        start = System.currentTimeMillis();
        GetTopProductsResult topProducts2 = productFacade.getTopProducts();
        end = System.currentTimeMillis();
        log.info("두번째 호출 끝");
        log.info("캐시 조회 소요시간 : {}", (end - start) + "ms");

        List<Long> orderIds = (List<Long>)redisTemplate.opsForValue().get(topOrderIdsKey);
        GetOrderProductsByOrderIdsResult getOrderProductsByOrderIdsResult = (GetOrderProductsByOrderIdsResult)redisTemplate.opsForValue().get(topProductIdsKey);
        List<Long> productIds = getOrderProductsByOrderIdsResult.getProductIds();
        GetProductsResult getProductsResult = (GetProductsResult)redisTemplate.opsForValue().get(topProductsKey);
        List<GetProductResult> products = getProductsResult.getProductResults();

        // then
        Assertions.assertThat(orderIds).isNotEmpty();
        Assertions.assertThat(orderIds).hasSizeLessThanOrEqualTo(5);
        Assertions.assertThat(productIds).isNotEmpty();
        Assertions.assertThat(productIds).hasSizeLessThanOrEqualTo(5);
        Assertions.assertThat(products).isNotEmpty();
        Assertions.assertThat(products).hasSizeLessThanOrEqualTo(5);
    }


}
