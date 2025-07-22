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

    @Test
    @DisplayName("상품 조회 테스트")
    public void getProduct(){
        // given
        Long productId = 1L;
        String productName = "샘플 상품명";
        int stock = 100;
        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product(productId, productName, new ProductStock(productId, stock), 10000)));

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
        Long productId = 1L;
        int initialStock = 100;
        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product(productId, "샘플 상품명", new ProductStock(productId, initialStock), 10000)));

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
        Long productId = 1L;
        int initialStock = 100;
        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product(productId, "샘플 상품명", new ProductStock(productId, initialStock), 10000)));
        when(productRepository.save(any(Product.class))).thenReturn(new Product(productId, "샘플 상품명", new ProductStock(productId, initialStock - orderQuantity), 10000));
        // when
        Product product = productService.reduceProductStock(productId, orderQuantity);

        // then
        assertThat(initialStock - orderQuantity).isEqualTo(product.getProductStock().getStockQuantity());
    }

    @Test
    @DisplayName("상품 목록 조회 테스트")
    public void getProductList() {
        // given
        List<Product> productList = List.of(
                new Product(1L, "상품1", new ProductStock(1L, 100), 10000),
                new Product(2L, "상품2", new ProductStock(2L, 200), 20000),
                new Product(3L, "상품3", new ProductStock(3L, 300), 30000)
        );
        when(productRepository.findAll()).thenReturn(productList);

        // when
        List<Product> products = productService.getProductList();

        // then
        assertThat(products).isNotEmpty();
        assertThat(products.size()).isEqualTo(3);
    }
}
