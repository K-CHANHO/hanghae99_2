package kr.hhplus.be.server.domain.product.application.service;

import kr.hhplus.be.server.domain.product.application.service.dto.*;
import kr.hhplus.be.server.domain.product.domain.entity.Product;
import kr.hhplus.be.server.domain.product.domain.entity.ProductStock;
import kr.hhplus.be.server.domain.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.domain.product.domain.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;

    public GetProductResult getProduct(GetProductCommand getProductCommand) {
        Product product = productRepository.findById(getProductCommand.getProductId())
                .orElseThrow(() -> new RuntimeException("상품을 조회할 수 없습니다."));
        ProductStock productStock = productStockRepository.findById(getProductCommand.getProductId())
                .orElseThrow(() -> new RuntimeException("상품 재고를 조회할 수 없습니다."));

        return GetProductResult.from(product, productStock);
    }

    public GetProductsResult getProducts(GetProductsCommand getProductsCommand) {

        List<Product> products = productRepository.findAllById(getProductsCommand.getProductIds());

        return GetProductsResult.from(products);
    }

    public ReduceStockResult reduceStock(ReduceStockCommand reduceStockCommand) {
        ProductStock productStock = productStockRepository.findByIdWithPessimisticLock(reduceStockCommand.getProductId())
                .orElseThrow(() -> new RuntimeException("상품 재고를 조회할 수 없습니다."));
        productStock.reduceStock(reduceStockCommand.getOrderQuantity());

        ProductStock reducedStock = productStockRepository.save(productStock);
        return ReduceStockResult.from(reducedStock);
    }


}
