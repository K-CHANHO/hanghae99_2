package kr.hhplus.be.server.domain.order.controller;

import com.google.gson.Gson;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import kr.hhplus.be.server.domain.order.presenter.controller.dto.OrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {
        "/testSql/cleanup.sql",
        "/testSql/balance.sql",
        "/testSql/product.sql",
        "/testSql/coupon.sql"
})
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("주문/결제 API 테스트_잔고부족")
    public void orderProcessWithLessBalance() throws Exception {
        // given
        String userId = "sampleUserId";
        ArrayList<OrderProductDto> orderProductDtoList = new ArrayList<>();
        OrderProductDto orderProductDto1 = OrderProductDto.builder().productId(4L).price(1000000).quantity(1).build();
        orderProductDtoList.add(orderProductDto1);

        OrderRequest body = new OrderRequest();
        body.setUserId(userId);
        body.setOrderProductDtoList(orderProductDtoList);

        Gson gson = new Gson();
        String url = "/api/v1/order";
        String requestBody = gson.toJson(body);

        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("잔고가 부족합니다."))
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    @DisplayName("주문/결제 API 테스트_재고부족")
    public void orderProcessWithLessStock() throws Exception {
        // given
        String userId = "sampleUserId";
        ArrayList<OrderProductDto> orderProductDtoList = new ArrayList<>();
        OrderProductDto orderProductDto1 = OrderProductDto.builder().productId(1L).price(10000).quantity(100).build();
        orderProductDtoList.add(orderProductDto1);

        OrderRequest body = new OrderRequest();
        body.setUserId(userId);
        body.setOrderProductDtoList(orderProductDtoList);

        Gson gson = new Gson();
        String url = "/api/v1/order";
        String requestBody = gson.toJson(body);

        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("재고가 부족합니다."))
                .andExpect(jsonPath("$.code").value(500));
    }


    @Test
    @DisplayName("주문/결제 API 테스트_성공")
    public void orderProcess() throws Exception {
        // given
        String userId = "sampleUserId";
        Long couponId = 1L;

        ArrayList<OrderProductDto> orderProductDtoList = new ArrayList<>();
        OrderProductDto orderProductDto1 = OrderProductDto.builder().productId(1L).price(10000).quantity(1).build();
        OrderProductDto orderProductDto2 = OrderProductDto.builder().productId(2L).price(20000).quantity(2).build();
        OrderProductDto orderProductDto3 = OrderProductDto.builder().productId(3L).price(30000).quantity(3).build();
        orderProductDtoList.add(orderProductDto1);
        orderProductDtoList.add(orderProductDto2);
        orderProductDtoList.add(orderProductDto3);

        OrderRequest body = new OrderRequest();
        body.setUserId(userId);
        body.setUserCouponId(couponId);
        body.setOrderProductDtoList(orderProductDtoList);

        Gson gson = new Gson();
        String url = "/api/v1/order";
        String requestBody = gson.toJson(body);

        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("주문/결제 성공"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.status").value("PAID"));

    }


}
