package kr.hhplus.be.server.domain.order.facade;

import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;

@SpringBootTest
@Sql(scripts = "/testSql/test-data.sql")
public class OrderFacadeIntegrationTest {
    @Autowired
    private OrderFacade orderFacade;

    @Test
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
}
