package kr.hhplus.be.server.domain.order.application.service;

import kr.hhplus.be.server.domain.order.application.event.OrderCompletedEvent;
import kr.hhplus.be.server.domain.order.application.event.OrderCreatedEvent;
import kr.hhplus.be.server.domain.order.application.service.dto.ChangeStatusCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.ChangeStatusResult;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderResult;
import kr.hhplus.be.server.domain.order.domain.entity.Order;
import kr.hhplus.be.server.domain.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.domain.payment.application.event.PaidEvent;
import kr.hhplus.be.server.domain.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public CreateOrderResult createOrder(CreateOrderCommand createOrderCommand) {
        Order order = Order.builder()
                .userId(createOrderCommand.getUserId())
                .status("PENDING")
                .totalPrice(createOrderCommand.getOrderProductDtoList().stream().mapToInt(orderProduct -> orderProduct.getPrice() * orderProduct.getQuantity()).sum())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        Order savedOrder = orderRepository.save(order);
        CreateOrderResult createOrderResult = CreateOrderResult.from(savedOrder);

        // 주문 생성 후 이벤트 발행
        publisher.publishEvent(new OrderCreatedEvent(createOrderCommand, createOrderResult));
        publisher.publishEvent(new OrderCompletedEvent(savedOrder.getOrderId(), order.getTotalPrice(), createOrderCommand.getOrderProductDtoList()));

        return createOrderResult;
    }

    public ChangeStatusResult changeStatus(ChangeStatusCommand changeStatusCommand) {
        Order order = orderRepository.findById(changeStatusCommand.getOrderId()).orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));
        order.changeStatus(changeStatusCommand.getStatus());

        Order changedOrder = orderRepository.save(order);
        return ChangeStatusResult.from(changedOrder);

    }

    @EventListener
    public void handlePaidEvent(PaidEvent event){
        ChangeStatusCommand changeStatusCommand = ChangeStatusCommand.builder()
                .orderId(event.getOrderId())
                .status("COMPLETED")
                .build();
        changeStatus(changeStatusCommand);
    }
}
