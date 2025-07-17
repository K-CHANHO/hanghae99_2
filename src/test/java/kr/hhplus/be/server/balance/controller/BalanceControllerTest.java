package kr.hhplus.be.server.balance.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class BalanceControllerTest {

    @InjectMocks
    private BalanceController balanceController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // MockMvc 초기화 및 설정
        mockMvc = MockMvcBuilders.standaloneSetup(balanceController).build();
    }

    @Test
    @DisplayName("잔액 조회 API 테스트")
    public void getBalanceTest() throws Exception {
        // given
        String url = "/api/v1/balance/{userId}";
        String userId = "sampleUserId";

        // when
        ResultActions result = mockMvc.perform(
                get(url, userId)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.balance").value(100000));
    }

    @Test
    @DisplayName("잔액 충전 API 테스트")
    public void chargeBalanceTest() throws Exception {
        // given
        String url = "/api/v1/balance";
        String userId = "sampleUserId";
        String requestBody = "{\"userId\" : \"sampleUserId\", \"amount\" : 10000, \"transactionId\" : \"tx12345\"}";

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
                .andExpect(jsonPath("$.data.newBalance").value(110000));
    }

}