package kr.hhplus.be.server.product.service;

import kr.hhplus.be.server.product.entity.Product;
import kr.hhplus.be.server.product.entity.ProductStock;
import kr.hhplus.be.server.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(stock).isEqualTo(product.getProductStock().getStock());
    }
}
