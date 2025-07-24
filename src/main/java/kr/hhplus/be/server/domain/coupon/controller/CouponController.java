package kr.hhplus.be.server.domain.coupon.controller;

import kr.hhplus.be.server.apidocs.CouponApiDocs;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.coupon.dto.IssueCouponRequest;
import kr.hhplus.be.server.domain.coupon.dto.IssueCouponResponse;
import kr.hhplus.be.server.domain.coupon.entity.UserCoupon;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponController implements CouponApiDocs {

    private final CouponService couponService;

    @PostMapping("/issue")
    public ResponseEntity<ApiResponse<IssueCouponResponse>> issueCoupon(@RequestBody IssueCouponRequest request) {

        UserCoupon userCoupon = couponService.issueCoupon(request.getUserId(), request.getCouponId());
        IssueCouponResponse response = new IssueCouponResponse();
        response.from(userCoupon);

        ApiResponse<IssueCouponResponse> result = new ApiResponse<>();
        result.setCode(200);
        result.setMessage("쿠폰 발급 성공");
        result.setData(response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
