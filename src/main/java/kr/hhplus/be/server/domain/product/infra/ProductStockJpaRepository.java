package kr.hhplus.be.server.domain.product.infra;

import kr.hhplus.be.server.domain.product.domain.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockJpaRepository extends JpaRepository<ProductStock, Long> {
}
