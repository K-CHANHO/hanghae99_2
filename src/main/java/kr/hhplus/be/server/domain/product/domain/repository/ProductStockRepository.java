package kr.hhplus.be.server.domain.product.domain.repository;

import kr.hhplus.be.server.domain.product.domain.entity.ProductStock;

import java.util.Optional;

public interface ProductStockRepository {
    Optional<ProductStock> findById(Long productId);

    ProductStock save(ProductStock productStock);
}
