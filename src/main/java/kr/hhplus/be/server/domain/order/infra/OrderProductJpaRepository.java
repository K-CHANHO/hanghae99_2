package kr.hhplus.be.server.domain.order.infra;

import kr.hhplus.be.server.domain.order.domain.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderProductJpaRepository extends JpaRepository<OrderProduct, Long> {
    @Query(value = "SELECT product_id " +
            "FROM order_product " +
            "WHERE order_id IN (:orderIds) " +
            "GROUP BY product_id " +
            "ORDER BY count(product_id) DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Long> findTop5OrderProducts(List<Long> orderIds);
}
