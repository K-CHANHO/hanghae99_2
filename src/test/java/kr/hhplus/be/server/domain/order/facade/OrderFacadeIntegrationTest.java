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
import kr.hhplus.be.server.domain.product.domain.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;

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

}
