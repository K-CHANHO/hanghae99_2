package kr.hhplus.be.server.order.service;

import kr.hhplus.be.server.order.entity.Order;
import kr.hhplus.be.server.order.entity.OrderProduct;
import kr.hhplus.be.server.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문 생성 테스트")
    public void createOrderTest(){
        // given
        Long orderId = 1L;
        String userId = "sampleUserId";
        String status = "PENDING";
        int totalPrice = 100000;

        Order order = new Order(orderId, userId, status, totalPrice, new Timestamp(System.currentTimeMillis()), new ArrayList<OrderProduct>());
        when(orderRepository.save(any())).thenReturn(order);

        // when
        Order createdOrder = orderService.createOrder(orderId, userId, status, totalPrice, new ArrayList<OrderProduct>());

        // then
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getOrderId()).isEqualTo(orderId);
        assertThat(createdOrder.getTotalPrice()).isEqualTo(totalPrice);

    }


}
