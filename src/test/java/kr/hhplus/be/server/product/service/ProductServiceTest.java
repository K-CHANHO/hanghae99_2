package kr.hhplus.be.server.product.service;

import kr.hhplus.be.server.product.entity.Product;
import kr.hhplus.be.server.product.entity.ProductStock;
import kr.hhplus.be.server.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 조회 테스트")
    public void getProduct(){
        // given
        String productId = "sampleProductId";
        String productName = "샘플 상품명";
        int stock = 100;
        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product(productId, productName, new ProductStock(productId, stock))));

        // when
        Product product = productService.getProduct(productId);

        // then
        assertThat(product).isNotNull();
        assertThat(productId).isEqualTo(product.getProductId());
        assertThat(productName).isEqualTo(product.getProductName());
        assertThat(stock).isEqualTo(product.getProductStock().getStockQuantity());
    }

    @ParameterizedTest
    @ValueSource(ints = {101, 150, 999})
    @DisplayName("상품 재고 차감 실패 테스트 - 재고 부족")
    public void reduceProductStockFail(int orderQuantity){
        // given
        String productId = "sampleProductId";
        int initialStock = 100;
        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product(productId, "샘플 상품명", new ProductStock(productId, initialStock))));

        // when, then
        assertThatThrownBy(() -> productService.reduceProductStock(productId, orderQuantity))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("재고가 부족합니다.");

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 50, 100})
    @DisplayName("상품 재고 차감 테스트")
    public void reduceProductStock(int orderQuantity){
        // given
        String productId = "sampleProductId";
        int initialStock = 100;
        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product(productId, "샘플 상품명", new ProductStock(productId, initialStock))));
        when(productRepository.save(any(Product.class))).thenReturn(new Product(productId, "샘플 상품명", new ProductStock(productId, initialStock - orderQuantity)));
        // when
        Product product = productService.reduceProductStock(productId, orderQuantity);

        // then
        assertThat(initialStock - orderQuantity).isEqualTo(product.getProductStock().getStockQuantity());
    }
}
