package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import kr.hhplus.be.server.domain.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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
    @Mock
    private ProductStockRepository productStockRepository;

    @Test
    @DisplayName("상품 조회 테스트")
    public void getProduct(){
        // given
        Long productId = 1L;
        String productName = "샘플 상품명";
        int stock = 100;
        when(productRepository.findById(productId)).thenReturn(Optional.of(Product.builder().productId(productId).productName(productName).price(10000).build()));
        when(productStockRepository.findById(productId)).thenReturn(Optional.of(ProductStock.builder().productId(productId).stockQuantity(stock).build()));

        // when
        Product product = productService.getProduct(productId);

        // then
        assertThat(product).isNotNull();
        assertThat(productId).isEqualTo(product.getProductId());
        assertThat(productName).isEqualTo(product.getProductName());
        assertThat(product.getProductStock().getStockQuantity()).isEqualTo(stock);
    }

    @ParameterizedTest
    @ValueSource(ints = {101, 150, 999})
    @DisplayName("상품 재고 차감 실패 테스트 - 재고 부족")
    public void reduceProductStockFail(int orderQuantity){
        // given
        Long productId = 1L;
        int initialStock = 100;
        when(productStockRepository.findById(productId)).thenReturn(Optional.of(new ProductStock(productId, initialStock)));

        // when, then
        assertThatThrownBy(() -> productService.reduceStock(productId, orderQuantity))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("재고가 부족합니다.");

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 50, 100})
    @DisplayName("상품 재고 차감 테스트")
    public void reduceProductStock(int orderQuantity){
        // given
        Long productId = 1L;
        int initialStock = 100;
        when(productStockRepository.findById(productId)).thenReturn(Optional.of(new ProductStock(productId, 10000)));
        when(productStockRepository.save(any(ProductStock.class))).thenReturn(new ProductStock(productId,  initialStock - orderQuantity));

        // when
        ProductStock productStock = productService.reduceStock(productId, orderQuantity);

        // then
        assertThat(initialStock - orderQuantity).isEqualTo(productStock.getStockQuantity());
    }

    @Test
    void getProducts() {
        // given
        List<Long> productIds = List.of(1L, 2L, 3L, 4L, 5L);
        List<Product> mockProducts = List.of(
                Product.builder().productId(1L).productName("Product 1").price(10000).build(),
                Product.builder().productId(2L).productName("Product 2").price(20000).build(),
                Product.builder().productId(3L).productName("Product 3").price(30000).build(),
                Product.builder().productId(4L).productName("Product 4").price(40000).build(),
                Product.builder().productId(5L).productName("Product 5").price(50000).build()
        );
        when(productRepository.findAllById(productIds)).thenReturn(mockProducts);

        // when
        List<Product> products = productService.getProducts(productIds);

        // then
        assertThat(products).isNotNull();
        assertThat(products).hasSizeLessThanOrEqualTo(5);
    }
}
