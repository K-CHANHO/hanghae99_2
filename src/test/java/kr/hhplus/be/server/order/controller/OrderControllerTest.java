package kr.hhplus.be.server.order.controller;

import com.google.gson.Gson;
import kr.hhplus.be.server.order.dto.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // MockMvc 초기화 및 설정
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void createOrder() throws Exception {
        // given
        String url = "/api/v1/order";
        OrderRequest request = new OrderRequest();
        request.setUserId("sampleUserId");
        request.setUserCouponId("sampleCouponId");

        OrderRequest.Product product1 = new OrderRequest.Product();
        product1.setProductId("Product1");
        product1.setPrice(100000);
        product1.setQuantity(3);

        OrderRequest.Product product2 = new OrderRequest.Product();
        product2.setProductId("Product1");
        product2.setPrice(100000);
        product2.setQuantity(3);

        request.setProducts(List.of(product1, product2));

        Gson gson = new Gson();
        String content = gson.toJson(request);

        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("주문 성공"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.orderId").isNotEmpty())
                .andExpect(jsonPath("$.data.userId").value("sampleUserId"))
                .andExpect(jsonPath("$.data.totalPrice").value(600000))
                .andExpect(jsonPath("$.data.status").value("CREATED"))
                .andExpect(jsonPath("$.data.orderDate").isNotEmpty());
    }
}