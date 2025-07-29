package kr.hhplus.be.server.domain.product.infra;

import kr.hhplus.be.server.domain.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
}
