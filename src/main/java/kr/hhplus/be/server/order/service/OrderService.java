package kr.hhplus.be.server.order.service;

import kr.hhplus.be.server.order.entity.Order;
import kr.hhplus.be.server.order.entity.OrderProduct;
import kr.hhplus.be.server.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(String userId, ArrayList<OrderProduct> orderProducts) {
        Order order = Order.builder()
                .userId(userId)
                .status("PENDING")
                .orderProductList(orderProducts)
                .totalPrice(orderProducts.stream().mapToInt(OrderProduct::getTotalPrice).sum())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
        return orderRepository.save(order);
    }

    public Order changeStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));
        order.setStatus(status);

        return orderRepository.save(order);

    }
}
