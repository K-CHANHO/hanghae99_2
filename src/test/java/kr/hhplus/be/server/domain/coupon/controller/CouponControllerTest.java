package kr.hhplus.be.server.domain.coupon.controller;

import kr.hhplus.be.server.domain.coupon.controller.CouponController;
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
class CouponControllerTest {

    @InjectMocks
    private CouponController couponController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // MockMvc 초기화 및 설정
        mockMvc = MockMvcBuilders.standaloneSetup(couponController).build();
    }

    @Test
    void issueCoupon() throws Exception {
        // gien
        String url = "/api/v1/coupon/issue";
        String requestBody = "{ \"userId\": \"sampleUserId\", \"couponId\": \"COUPON1\" }";

        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("쿠폰 발급 성공"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.couponId").value("COUPON1"))
                .andExpect(jsonPath("$.data.ruleId").value("RULE1"))
                .andExpect(jsonPath("$.data.couponName").value("깜짝 10% 할인쿠폰"))
                .andExpect(jsonPath("$.data.status").value("ING"))
                .andExpect(jsonPath("$.data.totalQuantity").value(100))
                .andExpect(jsonPath("$.data.remainQuantity").value(50));

    }
}