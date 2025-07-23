package kr.hhplus.be.server.product.service;

import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.product.service.ProductService;
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
        Long productId = 1L;
        String productName = "샘플 상품명";
        int stock = 100;
        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product(productId, productName, 10000)));

        // when
        Product product = productService.getProduct(productId);

        // then
        assertThat(product).isNotNull();
        assertThat(productId).isEqualTo(product.getProductId());
        assertThat(productName).isEqualTo(product.getProductName());
    }

//    @Test
//    @DisplayName("상품 목록 조회 테스트")
//    public void getProductList() {
//        // given
//        List<Product> productList = List.of(
//                new Product(1L, "상품1", 10000),
//                new Product(2L, "상품2", 20000),
//                new Product(3L, "상품3", 30000)
//        );
//        when(productRepository.findAll()).thenReturn(productList);
//
//        // when
//        List<Product> products = productService.getProductList();
//
//        // then
//        assertThat(products).isNotEmpty();
//        assertThat(products.size()).isEqualTo(3);
//    }
}
