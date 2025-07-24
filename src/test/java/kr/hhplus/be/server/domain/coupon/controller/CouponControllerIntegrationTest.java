package kr.hhplus.be.server.domain.coupon.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/testSql/test-data.sql")
class CouponControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("쿠폰 발급 API 테스트")
    void issueCoupon() throws Exception {
        // given
        Long couponId = 1L;
        String userId = "sampleUserId";
        String url = "/api/v1/coupon/issue";
        String requestBody = "{ \"userId\": \"" + userId + "\", \"couponId\": "+ couponId +" }";

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
                .andExpect(jsonPath("$.data.couponId").value(couponId))
                .andExpect(jsonPath("$.data.couponName").value("깜짝 10% 할인쿠폰"));

    }
}