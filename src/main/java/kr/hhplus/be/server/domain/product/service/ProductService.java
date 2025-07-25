package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;

    public Product getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 조회할 수 없습니다."));
        ProductStock productStock = productStockRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품 재고를 조회할 수 없습니다."));

        product.setProductStock(productStock);
        return product;
    }

    public List<Product> getProducts(List<Long> orderProductIds) {
        return productRepository.findAllById(orderProductIds);
    }

    public ProductStock reduceStock(Long productId, int orderQuantity) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        ProductStock productStock = productStockRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품 재고를 조회할 수 없습니다."));
        productStock.reduceStock(orderQuantity);
        lock.unlock();
        return productStockRepository.save(productStock);
    }


}
