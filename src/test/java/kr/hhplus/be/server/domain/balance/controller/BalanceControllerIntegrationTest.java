package kr.hhplus.be.server.domain.balance.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
public class BalanceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("잔액 조회 API 테스트")
    public void getBalance() throws Exception {
        // given
        String url = "/api/v1/balance/{userId}";
        String userId = "sampleUserId";

        // when
        ResultActions result = mockMvc.perform(
                get(url, userId)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("잔액 조회 성공"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.balance").value(300000));
    }

    @Test
    @DisplayName("잔액 충전 API 테스트_음수 값 입력받은 경우")
    public void chargeBalanceWithNegativeValue() throws Exception {
        // given
        String url = "/api/v1/balance";
        String userId = "sampleUserId";
        int chargeAmount = -100000;
        String requestBody = "{\"userId\" : \"" + userId + "\", \"amount\" : " + chargeAmount + ", \"transactionId\" : \"tx12345\"}";

        // when
        ResultActions result = mockMvc.perform(
                patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("충전은 양수 값만 가능합니다."))
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    @DisplayName("잔액 충전 API 테스트_성공")
    public void chargeBalance() throws Exception {
        // given
        String url = "/api/v1/balance";
        String userId = "sampleUserId";
        String requestBody = "{\"userId\" : \"sampleUserId\", \"amount\" : 100000, \"transactionId\" : \"tx12345\"}";

        // when
        ResultActions result = mockMvc.perform(
                patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("잔액 충전 성공"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.newBalance").value(400000));
    }

}