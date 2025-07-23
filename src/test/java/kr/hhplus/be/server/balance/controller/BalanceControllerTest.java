package kr.hhplus.be.server.balance.controller;

import kr.hhplus.be.server.domain.balance.controller.BalanceController;
import kr.hhplus.be.server.domain.balance.entity.Balance;
import kr.hhplus.be.server.domain.balance.repository.BalanceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class BalanceControllerTest {

    @Autowired
    private BalanceController balanceController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BalanceRepository balanceRepository;

    @BeforeEach
    void setUp() {
        Balance balance = new Balance("sampleUserId", 100000);
        balanceRepository.save(balance);
    }

    @AfterEach
    void cleanUp(){
        balanceRepository.deleteAll();
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
                .andExpect(jsonPath("$.message").value("잔액 조회 성공"))
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