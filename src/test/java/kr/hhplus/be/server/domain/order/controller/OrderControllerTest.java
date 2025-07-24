package kr.hhplus.be.server.domain.order.controller;

import com.google.gson.Gson;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import kr.hhplus.be.server.domain.order.dto.OrderRequest;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.facade.OrderFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;
    private MockMvc mockMvc;
    @Mock
    private OrderFacade orderFacade;

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
        request.setUserCouponId(1L);

        OrderProductDto orderProductDto1 = new OrderProductDto();
        orderProductDto1.setProductId(1L);
        orderProductDto1.setPrice(100000);
        orderProductDto1.setQuantity(3);

        OrderProductDto orderProductDto2 = new OrderProductDto();
        orderProductDto2.setProductId(2L);
        orderProductDto2.setPrice(100000);
        orderProductDto2.setQuantity(3);

        request.setOrderProductDtoList(List.of(orderProductDto1, orderProductDto2));

        Gson gson = new Gson();
        String content = gson.toJson(request);

        Order mockOrder = Order.builder()
                .orderId(1L)
                .userId("sampleUserId")
                .totalPrice(600000)
                .status("CREATED")
                .build();

        when(orderFacade.orderProcess(anyString(), anyList(), anyLong())).thenReturn(mockOrder);

        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("주문/결제 성공"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.orderId").isNotEmpty())
                .andExpect(jsonPath("$.data.userId").value("sampleUserId"))
                .andExpect(jsonPath("$.data.totalPrice").value(600000))
                .andExpect(jsonPath("$.data.status").value("CREATED"));
    }
}