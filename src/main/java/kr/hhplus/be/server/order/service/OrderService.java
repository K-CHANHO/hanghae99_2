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

    public Order createOrder(Long orderId, String userId, String status, int totalPrice, ArrayList<OrderProduct> orderProducts) {
        Order order = new Order(orderId, userId, status, totalPrice, new Timestamp(System.currentTimeMillis()), orderProducts);
        return orderRepository.save(order);
    }
}
