package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.order.application.service.OrderService;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderResult;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import kr.hhplus.be.server.domain.payment.domain.entity.Payment;
import kr.hhplus.be.server.domain.payment.domain.repository.PaymentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@Sql(scripts = {
        "/testSql/cleanup.sql",
        "/testSql/balance.sql",
        "/testSql/product.sql",
        "/testSql/coupon.sql"
})
public class OrderServiceIntegrationTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("주문 생성 후 이벤트 발행 테스트")
    public void testCreateOrderAndPublishEvent() {
         // Given
         CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                 .userId("sampleUserId")
                 .orderProductDtoList(List.of(
                         OrderProductDto.builder().productId(1L).price(1000).quantity(2).build(),
                         OrderProductDto.builder().productId(2L).price(2000).quantity(1).build()
                 ))
                 .userCouponId(1L)
                 .build();

         // When
         CreateOrderResult result = orderService.createOrder(createOrderCommand);
        Payment payment = paymentRepository.findByOrderId(result.getOrderId());

        // Then
         Assertions.assertNotNull(result);
         Assertions.assertEquals("PENDING", result.getStatus());
         Assertions.assertEquals("PAID", payment.getStatus());
    }

}
