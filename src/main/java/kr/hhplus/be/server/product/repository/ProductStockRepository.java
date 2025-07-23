package kr.hhplus.be.server.product.repository;

import kr.hhplus.be.server.product.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
}
