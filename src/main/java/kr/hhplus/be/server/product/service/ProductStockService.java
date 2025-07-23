package kr.hhplus.be.server.product.service;

import kr.hhplus.be.server.product.entity.ProductStock;
import kr.hhplus.be.server.product.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductStockService {
    private final ProductStockRepository productStockRepository;

    public ProductStock reduceStock(Long productId, int orderQuantity) {
        ProductStock productStock = productStockRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품 재고를 조회할 수 없습니다."));
        productStock.reduceStock(orderQuantity);
        return productStockRepository.save(productStock);
    }
}
