package kr.hhplus.be.server.domain.coupon.controller;

import kr.hhplus.be.server.apidocs.CouponApiDocs;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.coupon.dto.IssueCouponRequest;
import kr.hhplus.be.server.domain.coupon.dto.IssueCouponResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupon")
public class CouponController implements CouponApiDocs {

    @PostMapping("/issue")
    public ResponseEntity<ApiResponse<IssueCouponResponse>> issueCoupon(@RequestBody IssueCouponRequest request) {

        IssueCouponResponse response = new IssueCouponResponse();
        response.setCouponId("COUPON1");
        response.setRuleId("RULE1");
        response.setCouponName("깜짝 10% 할인쿠폰");
        response.setStatus("ING");
        response.setTotalQuantity(100);
        response.setRemainQuantity(50);

        ApiResponse<IssueCouponResponse> result = new ApiResponse<>();
        result.setCode(200);
        result.setMessage("쿠폰 발급 성공");
        result.setData(response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
