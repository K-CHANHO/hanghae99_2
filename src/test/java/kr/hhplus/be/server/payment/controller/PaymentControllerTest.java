package kr.hhplus.be.server.payment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // MockMvc 초기화 및 설정
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void processPayment() throws Exception {
        // given
        String url = "/api/v1/payment";
        String content = "{\"userId\":\"sampleUser\", \"orderId\":\"ORDER1\", \"price\":\"500000\"}";

        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("결제 성공"))
                .andExpect(jsonPath("$.data.paymentId").value("payment1"))
                .andExpect(jsonPath("$.data.orderId").value("ORDER1"))
                .andExpect(jsonPath("$.data.userId").value("sampleUser"))
                .andExpect(jsonPath("$.data.price").value("500000"));
    }
}