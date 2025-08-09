package kr.hhplus.be.server.domain.product.infra;

import kr.hhplus.be.server.domain.product.domain.entity.ProductStock;
import kr.hhplus.be.server.domain.product.domain.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductStockRepositoryImpl implements ProductStockRepository {

    private final ProductStockJpaRepository jpaRepository;

    @Override
    public Optional<ProductStock> findById(Long productId) {
        return jpaRepository.findById(productId);
    }

    @Override
    public Optional<ProductStock> findByIdWithPessimisticLock(Long productId) {
        return jpaRepository.findByIdWithPessimisticLock(productId);
    }

    @Override
    public ProductStock save(ProductStock productStock) {
        return jpaRepository.save(productStock);
    }
}
