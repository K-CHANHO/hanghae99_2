package kr.hhplus.be.server.domain.coupon.presenter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.hhplus.be.server.apidocs.CouponApiDocs;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.coupon.application.service.CouponService;
import kr.hhplus.be.server.domain.coupon.application.service.dto.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.presenter.controller.dto.IssueCouponRequest;
import kr.hhplus.be.server.domain.coupon.presenter.controller.dto.IssueCouponResponse;
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
    public ResponseEntity<ApiResponse<IssueCouponResponse>> issueCoupon(@RequestBody IssueCouponRequest request) throws JsonProcessingException {

        IssueCouponCommand couponCommand = new IssueCouponCommand(request);
//        IssueCouponResult couponResult = couponService.issueCoupon(couponCommand);
//        IssueCouponResponse response = new IssueCouponResponse(couponResult);
//        couponService.issueCouponRedis(couponCommand);
        couponService.issueCouponKafka(couponCommand);

        ApiResponse<IssueCouponResponse> result = new ApiResponse<>();
        result.setCode(200);
        result.setMessage("쿠폰 발급 요청 성공");
//        result.setData(response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
