package kr.hhplus.be.server.domain.order.infra;

import kr.hhplus.be.server.domain.order.domain.entity.Order;
import kr.hhplus.be.server.domain.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;

    @Override
    public Order save(Order order) {
        return jpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return jpaRepository.findById(orderId);
    }
}
