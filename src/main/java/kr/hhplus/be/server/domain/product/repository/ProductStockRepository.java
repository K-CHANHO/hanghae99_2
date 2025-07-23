package kr.hhplus.be.server.domain.product.repository;

import kr.hhplus.be.server.domain.product.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
}
