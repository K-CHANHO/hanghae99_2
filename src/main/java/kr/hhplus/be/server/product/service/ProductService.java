package kr.hhplus.be.server.product.service;

import kr.hhplus.be.server.product.entity.Product;
import kr.hhplus.be.server.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProduct(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 조회할 수 없습니다."));
    }

    public Product reduceProductStock(String productId, int orderQuantity) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("상품을 조회할 수 없습니다."));
        product.reduceStock(orderQuantity);

        return productRepository.save(product);
    }
}

