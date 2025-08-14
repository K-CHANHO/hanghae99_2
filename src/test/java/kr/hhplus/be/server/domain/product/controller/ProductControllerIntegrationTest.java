package kr.hhplus.be.server.domain.product.controller;

import kr.hhplus.be.server.domain.product.application.facade.ProductFacade;
import kr.hhplus.be.server.domain.product.application.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {
        "/testSql/cleanup.sql",
        "/testSql/order.sql",
        "/testSql/orderProduct.sql",
        "/testSql/payment.sql",
        "/testSql/product.sql",
})
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductFacade productFacade;

    @Test
    @DisplayName("상품 조회 API 테스트_없는 상품일 경우")
    public void getProduct() throws Exception {
        // given
        String url = "/api/v1/product/{productId}";

        // when
        ResultActions result = mockMvc.perform(
                get(url, 100L)
        );

        // then
        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("상품을 조회할 수 없습니다."))
                .andExpect(jsonPath("$.code").value(500));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    @DisplayName("상품 조회 API 테스트")
    public void getProduct(Long productId) throws Exception {
        // given
        String url = "/api/v1/product/{productId}";

        // when
        ResultActions result = mockMvc.perform(
                get(url, productId)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("상품 조회 성공"))
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("인기 상품 조회 API 테스트")
    public void getTopProducts() throws Exception {
        // given
        String url = "/api/v1/product/top";

        // when
        ResultActions result = mockMvc.perform(
                get(url)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("인기 상품 조회 성공"))
                .andExpect(jsonPath("$.code").value(200));
    }

}
