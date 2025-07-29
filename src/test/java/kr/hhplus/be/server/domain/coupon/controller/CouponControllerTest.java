package kr.hhplus.be.server.domain.coupon.controller;

import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.UserCoupon;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.coupon.service.dto.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.service.dto.IssueCouponResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CouponControllerTest {

    @InjectMocks
    private CouponController couponController;
    private MockMvc mockMvc;

    @Mock
    private CouponService couponService;

    @BeforeEach
    void setUp() {
        // MockMvc 초기화 및 설정
        mockMvc = MockMvcBuilders.standaloneSetup(couponController).build();
    }

    @Test
    @DisplayName("쿠폰 발급 테스트")
    void issueCoupon() throws Exception {
        // given
        String userId = "sampleUserId";
        Long couponId = 1L;
        Long userCouponId = 1L;
        String url = "/api/v1/coupon/issue";
        String requestBody = "{ \"userId\": \""+ userId +"\", \"couponId\": "+ couponId +" }";
        Coupon coupon = Coupon.builder()
                .couponId(couponId)
                .couponName("깜짝 10% 할인쿠폰")
                .discountRate(10)
                .quantity(100)
                .status("AVAILABLE")
                .build();
        UserCoupon userCoupon = UserCoupon.builder()
                .userCouponId(userCouponId)
                .status("AVAILABLE")
                .coupon(coupon)
                .build();
        IssueCouponResult couponResult = new IssueCouponResult(userCoupon);
        Mockito.when(couponService.issueCoupon(any(IssueCouponCommand.class))).thenReturn(couponResult);

        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        verify(couponService).issueCoupon(any(IssueCouponCommand.class));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("쿠폰 발급 성공"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userCouponId").value(userCouponId))
                .andExpect(jsonPath("$.data.couponId").value(couponId))
                .andExpect(jsonPath("$.data.couponName").value("깜짝 10% 할인쿠폰"));
    }
}