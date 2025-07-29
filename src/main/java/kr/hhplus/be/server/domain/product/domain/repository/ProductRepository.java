package kr.hhplus.be.server.domain.product.domain.repository;

import kr.hhplus.be.server.domain.product.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long productId);

    List<Product> findAllById(List<Long> orderProductIds);
}
