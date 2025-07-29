package kr.hhplus.be.server.domain.order.domain.repository;

import kr.hhplus.be.server.domain.order.domain.entity.Order;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(Long orderId);
}
