package kr.hhplus.be.server.domain.order.facade;

import kr.hhplus.be.server.domain.balance.application.service.dto.ViewBalanceCommand;
import kr.hhplus.be.server.domain.balance.application.service.dto.ViewBalanceResult;
import kr.hhplus.be.server.domain.balance.application.service.BalanceService;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.product.domain.entity.Product;
import kr.hhplus.be.server.domain.product.application.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

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


    // 재고 부족 -> 잔고 그대로 인지
    @Test
    @DisplayName("주문/결제 테스트_재고부족")
    public void orderProcessWithLessStock(){
        // given
        String userId = "sampleUserId";
        ViewBalanceCommand viewBalanceCommand = new ViewBalanceCommand(userId);
        ArrayList<OrderProductDto> orderProductDtoList = new ArrayList<>();
        OrderProductDto orderProductDto1 = OrderProductDto.builder().productId(1L).price(10000).quantity(100).build();
        orderProductDtoList.add(orderProductDto1);

        // when
        ViewBalanceResult serviceResponseDto = balanceService.getBalance(viewBalanceCommand);

        // then
        assertThatThrownBy(() -> orderFacade.orderProcess(userId, orderProductDtoList, null))
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

        // when
        Product product = productService.getProduct(4L);

        // then
        assertThatThrownBy(() -> orderFacade.orderProcess(userId, orderProductDtoList, null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("잔고가 부족합니다.");
        assertThat(product.getProductStock().getStockQuantity()).isEqualTo(10);

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

        // when
        Order order = orderFacade.orderProcess(userId, orderProductDtoList, couponId);
        ViewBalanceCommand viewBalanceCommand = new ViewBalanceCommand(order.getUserId());
        ViewBalanceResult viewBalanceResult = balanceService.getBalance(viewBalanceCommand);


        // then
        assertThat(order).isNotNull();
        assertThat(order.getStatus()).isEqualTo("PAID");
        assertThat(order.getTotalPrice()).isEqualTo(140000);
        assertThat(viewBalanceResult.getBalance()).isEqualTo((int) (300000 - 140000*0.9));
    }

}
