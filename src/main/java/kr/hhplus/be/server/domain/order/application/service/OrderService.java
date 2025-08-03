package kr.hhplus.be.server.domain.order.application.service;

import kr.hhplus.be.server.domain.order.application.service.dto.ChangeStatusCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.ChangeStatusResult;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderResult;
import kr.hhplus.be.server.domain.order.domain.entity.Order;
import kr.hhplus.be.server.domain.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public CreateOrderResult createOrder(CreateOrderCommand createOrderCommand) {
        Order order = Order.builder()
                .userId(createOrderCommand.getUserId())
                .status("PENDING")
                .totalPrice(createOrderCommand.getOrderProductDtoList().stream().mapToInt(orderProduct -> orderProduct.getPrice() * orderProduct.getQuantity()).sum())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        Order savedOrder = orderRepository.save(order);
        return CreateOrderResult.from(savedOrder);
    }

    public ChangeStatusResult changeStatus(ChangeStatusCommand changeStatusCommand) {
        Order order = orderRepository.findById(changeStatusCommand.getOrderId()).orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));
        order.changeStatus(changeStatusCommand.getStatus());

        Order changedOrder = orderRepository.save(order);
        return ChangeStatusResult.from(changedOrder);

    }
}
