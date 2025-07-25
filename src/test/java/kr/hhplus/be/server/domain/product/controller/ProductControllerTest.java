package kr.hhplus.be.server.domain.product.controller;

import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.facade.ProductFacade;
import kr.hhplus.be.server.domain.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;
    @Mock
    private ProductFacade productFacade;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // MockMvc 초기화 및 설정
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @DisplayName("상품 조회 API 테스트")
    void viewProduct() throws Exception {
        // given
        Long productId = 1L;
        String url = "/api/v1/product/{productId}";
        Product product = Product.builder()
                .productId(productId)
                .productName("항해 백엔드 9기 과정")
                .price(1500000)
                .productStock(ProductStock.builder().productId(productId).stockQuantity(100).build())
                .build();
        when(productService.getProduct(productId)).thenReturn(product);

        // when
        ResultActions result = mockMvc.perform(
                get(url, productId)
        );

        // then
        verify(productService).getProduct(productId);
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("상품 조회 성공"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.productId").value(productId))
                .andExpect(jsonPath("$.data.productName").value("항해 백엔드 9기 과정"))
                .andExpect(jsonPath("$.data.price").value(1500000))
                .andExpect(jsonPath("$.data.stock").value(100));
    }

    @Test
    @DisplayName("인기 상품 조회 API 테스트")
    void viewTopProducts() throws Exception {
        // given
        String url = "/api/v1/product/top";
        ProductStock productStock1 = ProductStock.builder().productId(1L).stockQuantity(10).build();
        ProductStock productStock2 = ProductStock.builder().productId(2L).stockQuantity(20).build();
        ProductStock productStock3 = ProductStock.builder().productId(2L).stockQuantity(30).build();
        List<Product> mockTopProducts = List.of(
                Product.builder().productId(1L).productName("항해 백엔드 9기 과정").price(1500000).productStock(productStock1).build(),
                Product.builder().productId(2L).productName("항해 프론트엔드 9기 과정").price(1200000).productStock(productStock2).build(),
                Product.builder().productId(3L).productName("항해 데브옵스 9기 과정").price(1600000).productStock(productStock3).build()
        );
        when(productFacade.getTopProducts()).thenReturn(mockTopProducts);


        // when
        ResultActions result = mockMvc.perform(
                get(url)
        );

        // then
        //verify(productService)
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("인기 상품 조회 성공"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.topProducts").isArray())
                .andExpect(jsonPath("$.data.topProducts.size()").value(3))
                .andExpect(jsonPath("$.data.topProducts[0].productId").value(1L))
                .andExpect(jsonPath("$.data.topProducts[0].productName").value("항해 백엔드 9기 과정"))
                .andExpect(jsonPath("$.data.topProducts[0].stock").value(10))
                .andExpect(jsonPath("$.data.topProducts[0].price").value(1500000))

                .andExpect(jsonPath("$.data.topProducts[1].productId").value(2L))
                .andExpect(jsonPath("$.data.topProducts[1].productName").value("항해 프론트엔드 9기 과정"))
                .andExpect(jsonPath("$.data.topProducts[1].stock").value(20))
                .andExpect(jsonPath("$.data.topProducts[1].price").value(1200000))

                .andExpect(jsonPath("$.data.topProducts[2].productId").value(3L))
                .andExpect(jsonPath("$.data.topProducts[2].productName").value("항해 데브옵스 9기 과정"))
                .andExpect(jsonPath("$.data.topProducts[2].stock").value(30))
                .andExpect(jsonPath("$.data.topProducts[2].price").value(1600000))
        ;
    }
}