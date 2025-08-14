package kr.hhplus.be.server.domain.balance.controller;

import kr.hhplus.be.server.domain.balance.presenter.controller.BalanceController;
import kr.hhplus.be.server.domain.balance.application.service.dto.ChargeBalanceCommand;
import kr.hhplus.be.server.domain.balance.application.service.dto.ChargeBalanceResult;
import kr.hhplus.be.server.domain.balance.application.service.dto.GetBalanceCommand;
import kr.hhplus.be.server.domain.balance.application.service.dto.GetBalanceResult;
import kr.hhplus.be.server.domain.balance.application.service.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class BalanceControllerTest {

    @InjectMocks
    private BalanceController balanceController;

    @Mock
    private BalanceService balanceService;

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
        GetBalanceResult getBalanceResult = GetBalanceResult.builder().userId(userId).balance(100000).build();
        when(balanceService.getBalance(any(GetBalanceCommand.class))).thenReturn(getBalanceResult);

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

    @ParameterizedTest
    @ValueSource(ints = {10000, 50000, 100000})
    @DisplayName("잔액 충전 API 테스트")
    public void chargeBalanceTest(int chargeAmount) throws Exception {
        // given
        String url = "/api/v1/balance";
        String userId = "sampleUserId";
        String requestBody = "{\"userId\" : \"" + userId+ "\", \"amount\" : \""+ chargeAmount + "\", \"transactionId\" : \"tx12345\"}";

        ChargeBalanceResult serviceResponse = ChargeBalanceResult.builder().userId(userId).balance(100000 + chargeAmount).build();
        when(balanceService.chargeBalance(any(ChargeBalanceCommand.class))).thenReturn(serviceResponse);

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
                .andExpect(jsonPath("$.data.newBalance").value(100000 + chargeAmount));
    }

}