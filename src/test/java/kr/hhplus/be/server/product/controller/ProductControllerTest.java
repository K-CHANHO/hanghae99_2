package kr.hhplus.be.server.product.controller;

import kr.hhplus.be.server.domain.product.controller.ProductController;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
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
                .andExpect(jsonPath("$.data.topProducts.size()").value(5))
                .andExpect(jsonPath("$.data.topProducts[0].productId").value("product1"))
                .andExpect(jsonPath("$.data.topProducts[0].productName").value("항해 백엔드 9기 과정"))
                .andExpect(jsonPath("$.data.topProducts[0].soldCount").value(100))
                .andExpect(jsonPath("$.data.topProducts[0].price").value(1500000))

                .andExpect(jsonPath("$.data.topProducts[1].productId").value("product2"))
                .andExpect(jsonPath("$.data.topProducts[1].productName").value("항해 프론트엔드 9기 과정"))
                .andExpect(jsonPath("$.data.topProducts[1].soldCount").value(50))
                .andExpect(jsonPath("$.data.topProducts[1].price").value(1200000))

                .andExpect(jsonPath("$.data.topProducts[2].productId").value("product3"))
                .andExpect(jsonPath("$.data.topProducts[2].productName").value("항해 데브옵스 9기 과정"))
                .andExpect(jsonPath("$.data.topProducts[2].soldCount").value(70))
                .andExpect(jsonPath("$.data.topProducts[2].price").value(1600000))

                .andExpect(jsonPath("$.data.topProducts[3].productId").value("product4"))
                .andExpect(jsonPath("$.data.topProducts[3].productName").value("항해 풀스택 9기 과정"))
                .andExpect(jsonPath("$.data.topProducts[3].soldCount").value(20))
                .andExpect(jsonPath("$.data.topProducts[3].price").value(2000000))

                .andExpect(jsonPath("$.data.topProducts[4].productId").value("product5"))
                .andExpect(jsonPath("$.data.topProducts[4].productName").value("항해 학습메이트 9기 과정"))
                .andExpect(jsonPath("$.data.topProducts[4].soldCount").value(5))
                .andExpect(jsonPath("$.data.topProducts[4].price").value(1000000))
        ;
    }
}