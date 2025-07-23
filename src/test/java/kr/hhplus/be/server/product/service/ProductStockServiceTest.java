package kr.hhplus.be.server.product.service;

import kr.hhplus.be.server.product.entity.ProductStock;
import kr.hhplus.be.server.product.repository.ProductStockRepository;
import org.junit.jupiter.api.DisplayName;
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
public class ProductStockServiceTest {
    @InjectMocks
    private ProductStockService productStockService;
    @Mock
    private ProductStockRepository productStockRepository;

    @ParameterizedTest
    @ValueSource(ints = {101, 150, 999})
    @DisplayName("상품 재고 차감 실패 테스트 - 재고 부족")
    public void reduceProductStockFail(int orderQuantity){
        // given
        Long productId = 1L;
        int initialStock = 100;
        when(productStockRepository.findById(productId)).thenReturn(Optional.of(new ProductStock(productId, initialStock)));

        // when, then
        assertThatThrownBy(() -> productStockService.reduceStock(productId, orderQuantity))
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
        ProductStock productStock = productStockService.reduceStock(productId, orderQuantity);

        // then
        assertThat(initialStock - orderQuantity).isEqualTo(productStock.getStockQuantity());
    }
}
