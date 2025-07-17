package kr.hhplus.be.server.product.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;
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
        String productId = "product123";
        String url = "/api/v1/product/{productId}";

        // when
        ResultActions result = mockMvc.perform(
                get(url, productId)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("상품 조회 성공"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.productId").value(productId))
                .andExpect(jsonPath("$.data.productName").value("항해 백엔드 9기 과정"))
                .andExpect(jsonPath("$.data.price").value(1500000))
                .andExpect(jsonPath("$.data.stock").value(100));
    }
}