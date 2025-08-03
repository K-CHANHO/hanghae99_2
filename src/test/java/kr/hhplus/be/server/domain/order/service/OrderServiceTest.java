package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.order.application.service.OrderService;
import kr.hhplus.be.server.domain.order.application.service.dto.ChangeStatusCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.ChangeStatusResult;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderResult;
import kr.hhplus.be.server.domain.order.domain.entity.Order;
import kr.hhplus.be.server.domain.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

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

        Order order = Order.builder()
                .orderId(orderId)
                .userId(userId)
                .status("PENDING")
                .totalPrice(totalPrice)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .userId(userId)
                .orderProductDtoList(new ArrayList<OrderProductDto>())
                .build();

        when(orderRepository.save(any())).thenReturn(order);

        // when
        CreateOrderResult createdOrder = orderService.createOrder(createOrderCommand);

        // then
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getOrderId()).isEqualTo(orderId);
        assertThat(createdOrder.getTotalPrice()).isEqualTo(totalPrice);

    }

    @Test
    @DisplayName("주문 상태 변경 테스트")
    public void changeOrderStatus(){
        // given
        Long orderId = 1L;
        String status = "PAID";
        ChangeStatusCommand changeStatusCommand = ChangeStatusCommand.builder()
                .orderId(orderId)
                .status(status)
                .build();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(Order.builder().orderId(orderId).status("PENDING").build()));
        when(orderRepository.save(any(Order.class))).thenReturn((Order.builder().orderId(orderId).status("PAID").build()));

        // when
        ChangeStatusResult changeStatusResult = orderService.changeStatus(changeStatusCommand);

        // then
        assertThat(changeStatusResult.getStatus()).isEqualTo(status);
    }


}
