package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.domain.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    @Query("SELECT op FROM OrderProduct op WHERE op.order.id IN :orderIds GROUP BY op.product.id ORDER BY COUNT(op.product.id) DESC")
    List<OrderProduct> findTopOrderProducts(List<Long> orderIds);
}
