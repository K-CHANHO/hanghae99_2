package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(String userId, List<OrderProductDto> orderProducts) {
        Order order = Order.builder()
                .userId(userId)
                .status("PENDING")
                .totalPrice(orderProducts.stream().mapToInt(orderProduct -> orderProduct.getPrice() * orderProduct.getQuantity()).sum())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
        return orderRepository.save(order);
    }

    public Order changeStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));
        order.changeStatus(status);

        return orderRepository.save(order);

    }
}
