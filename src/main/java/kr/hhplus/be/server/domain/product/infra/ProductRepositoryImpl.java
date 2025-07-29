package kr.hhplus.be.server.domain.product.infra;

import kr.hhplus.be.server.domain.product.domain.entity.Product;
import kr.hhplus.be.server.domain.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository jpaRepository;

    @Override
    public Optional<Product> findById(Long productId) {
        return jpaRepository.findById(productId);
    }

    @Override
    public List<Product> findAllById(List<Long> orderProductIds) {
        return jpaRepository.findAllById(orderProductIds);
    }
}
