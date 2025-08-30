package kr.hhplus.be.server.domain.order.controller;

import com.google.gson.Gson;
import kr.hhplus.be.server.domain.order.application.facade.OrderFacade;
import kr.hhplus.be.server.domain.order.application.facade.dto.OrderProcessCommand;
import kr.hhplus.be.server.domain.order.application.facade.dto.OrderProcessResult;
import kr.hhplus.be.server.domain.order.application.service.OrderService;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderResult;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import kr.hhplus.be.server.domain.order.presenter.controller.OrderController;
import kr.hhplus.be.server.domain.order.presenter.controller.dto.OrderRequest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
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
    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        // MockMvc 초기화 및 설정
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void createOrder() throws Exception {
        // given
        String url = "/api/v1/order";

        OrderProductDto orderProductDto1 = new OrderProductDto();
        orderProductDto1.setProductId(1L);
        orderProductDto1.setPrice(100000);
        orderProductDto1.setQuantity(3);
        OrderProductDto orderProductDto2 = new OrderProductDto();
        orderProductDto2.setProductId(2L);
        orderProductDto2.setPrice(100000);
        orderProductDto2.setQuantity(3);

        OrderRequest request = OrderRequest.builder()
                .userId("sampleUserId")
                .userCouponId(1L)
                .orderProductDtoList(List.of(orderProductDto1, orderProductDto2))
                .build();

        Gson gson = new Gson();
        String content = gson.toJson(request);

        OrderProcessResult mockOrderProcessResult = OrderProcessResult.builder()
                .orderId(1L)
                .userId("sampleUserId")
                .totalPrice(600000)
                .status("PAID")
                .build();

        CreateOrderResult mockCreateOrderResult = CreateOrderResult.builder()
                .orderId(1L)
                .userId("sampleUserId")
                .totalPrice(600000)
                .status("PAID")
                .build();

        lenient().when(orderFacade.orderProcess(any(OrderProcessCommand.class))).thenReturn(mockOrderProcessResult);
        when(orderService.createOrder(any(CreateOrderCommand.class))).thenReturn(mockCreateOrderResult);

        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("주문 요청 성공"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.orderId").isNotEmpty())
                .andExpect(jsonPath("$.data.userId").value("sampleUserId"))
                .andExpect(jsonPath("$.data.totalPrice").value(600000))
                .andExpect(jsonPath("$.data.status").value("PAID"));
    }
}