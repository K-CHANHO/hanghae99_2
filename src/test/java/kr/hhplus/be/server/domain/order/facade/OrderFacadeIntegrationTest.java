package kr.hhplus.be.server.domain.order.facade;

import kr.hhplus.be.server.domain.balance.application.service.BalanceService;
import kr.hhplus.be.server.domain.balance.application.service.dto.ViewBalanceCommand;
import kr.hhplus.be.server.domain.balance.application.service.dto.ViewBalanceResult;
import kr.hhplus.be.server.domain.order.application.facade.OrderFacade;
import kr.hhplus.be.server.domain.order.application.facade.dto.OrderProcessCommand;
import kr.hhplus.be.server.domain.order.application.facade.dto.OrderProcessResult;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import kr.hhplus.be.server.domain.product.application.service.ProductService;
import kr.hhplus.be.server.domain.product.application.service.dto.GetProductCommand;
import kr.hhplus.be.server.domain.product.application.service.dto.GetProductResult;
import kr.hhplus.be.server.domain.product.domain.repository.ProductStockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Sql(scripts = {
        "/testSql/cleanup.sql",
        "/testSql/balance.sql",
        "/testSql/product.sql",
        "/testSql/coupon.sql"
})
public class OrderFacadeIntegrationTest {
    @Autowired
    private OrderFacade orderFacade;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductStockRepository productStockRepository;


    // 재고 부족 -> 잔고 그대로 인지
    @Test
    @DisplayName("주문/결제 테스트_재고부족")
    public void orderProcessWithLessStock(){
        // given
        String userId = "sampleUserId";
        ViewBalanceCommand viewBalanceCommand = ViewBalanceCommand.from(userId);
        ArrayList<OrderProductDto> orderProductDtoList = new ArrayList<>();
        OrderProductDto orderProductDto1 = OrderProductDto.builder().productId(1L).price(10000).quantity(100).build();
        orderProductDtoList.add(orderProductDto1);

        OrderProcessCommand orderProcessCommand = OrderProcessCommand.builder()
                .userId("sampleUserId")
                .userCouponId(1L)
                .orderProductDtoList(orderProductDtoList)
                .build();

        // when
        ViewBalanceResult serviceResponseDto = balanceService.getBalance(viewBalanceCommand);

        // then
        assertThatThrownBy(() -> orderFacade.orderProcess(orderProcessCommand))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("재고가 부족합니다.");
        assertThat(serviceResponseDto.getBalance()).isEqualTo(300000);

    }

    // 잔고 부족 -> 재고 그대로 인지
    @Test
    @DisplayName("주문/결제 테스트_잔고부족")
    public void orderProcessWithLessBalance(){
        // given
        String userId = "sampleUserId";
        ArrayList<OrderProductDto> orderProductDtoList = new ArrayList<>();
        OrderProductDto orderProductDto1 = OrderProductDto.builder().productId(4L).price(1000000).quantity(1).build();
        orderProductDtoList.add(orderProductDto1);

        OrderProcessCommand orderProcessCommand = OrderProcessCommand.builder()
                .userId("sampleUserId")
                .userCouponId(1L)
                .orderProductDtoList(orderProductDtoList)
                .build();

        GetProductCommand getProductCommand = GetProductCommand.from(4L);

        // when
        GetProductResult product = productService.getProduct(getProductCommand);

        // then
        assertThatThrownBy(() -> orderFacade.orderProcess(orderProcessCommand))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("잔고가 부족합니다.");
        assertThat(product.getStock()).isEqualTo(10);

    }

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
        OrderProcessCommand orderProcessCommand = OrderProcessCommand.builder()
                .userId(userId)
                .userCouponId(couponId)
                .orderProductDtoList(orderProductDtoList)
                .build();

        // when
        OrderProcessResult orderProcessResult = orderFacade.orderProcess(orderProcessCommand);
        ViewBalanceCommand viewBalanceCommand = ViewBalanceCommand.from(orderProcessResult.getUserId());
        ViewBalanceResult viewBalanceResult = balanceService.getBalance(viewBalanceCommand);


        // then
        assertThat(orderProcessResult).isNotNull();
        assertThat(orderProcessResult.getStatus()).isEqualTo("PAID");
        assertThat(orderProcessResult.getTotalPrice()).isEqualTo(140000);
        assertThat(viewBalanceResult.getBalance()).isEqualTo((int) (300000 - 140000*0.9));
    }

    @Test
    @DisplayName("주문/결제 - 재고 차감 동시성 테스트")
    public void orderProcessForProductStockReduce() throws InterruptedException {
        // given
        int threadCount = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        String userId = "sampleUserId";
        ArrayList<OrderProductDto> orderProductDtoList = new ArrayList<>();
        OrderProductDto orderProductDto1 = OrderProductDto.builder().productId(6L).price(10).quantity(1).build();
        orderProductDtoList.add(orderProductDto1);

        OrderProcessCommand orderProcessCommand = OrderProcessCommand.builder()
                .userId(userId)
                .orderProductDtoList(orderProductDtoList)
                .build();

        // when
        AtomicInteger failCnt = new AtomicInteger();
        for(int i = 0; i< threadCount; i++){
            int finalI = i;

            executorService.submit(() -> {
                try{
                    orderFacade.orderProcess(orderProcessCommand);
                } catch (Exception e){
                    failCnt.getAndIncrement();
                } finally{
                    latch.countDown();
                }
            });
        }
        latch.await();

        int stockQuantity = productStockRepository.findById(6L).get().getStockQuantity();

        // then
        assertThat(stockQuantity).isEqualTo(0);
        assertThat(failCnt.get()).isEqualTo(0);
    }

}
