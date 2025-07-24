package kr.hhplus.be.server.domain.order.facade;

import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Sql(scripts = "/testSql/test-data.sql")
public class OrderFacadeIntegrationTest {
    @Autowired
    private OrderFacade orderFacade;

    @Test
    @DisplayName("주문/결제 테스트")
    public void orderProcess(){
        // given
        String userId = "sampleUserId";
        ArrayList<OrderProductDto> orderProductDtoList = new ArrayList<>();
        OrderProductDto orderProductDto1 = OrderProductDto.builder().productId(1L).price(10000).quantity(1).build();
        OrderProductDto orderProductDto2 = OrderProductDto.builder().productId(2L).price(20000).quantity(2).build();
        OrderProductDto orderProductDto3 = OrderProductDto.builder().productId(3L).price(30000).quantity(3).build();
        orderProductDtoList.add(orderProductDto1);
        orderProductDtoList.add(orderProductDto2);
        orderProductDtoList.add(orderProductDto3);
        Long couponId = 1L;

        // when
        orderFacade.orderProcess(userId, orderProductDtoList, couponId);
    }

    @Test
    @DisplayName("재고 테스트")
    public void sdfas() throws InterruptedException {
        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // given
        String userId = "sampleUserId";
        ArrayList<OrderProductDto> orderProductDtoList = new ArrayList<>();
        OrderProductDto orderProductDto1 = OrderProductDto.builder().productId(1L).price(10000).quantity(1).build();
        orderProductDtoList.add(orderProductDto1);

        Long couponId = 1L;

        // when
        for (int i=0; i<threadCount; i++){
            executorService.execute(() -> {
                try {
                    orderFacade.orderProcess(userId, orderProductDtoList, couponId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });

        }
        latch.await();
    }

}
