package kr.hhplus.be.server.domain.order.infra;

import kr.hhplus.be.server.domain.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {


}
