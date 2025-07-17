package kr.hhplus.be.server.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.coupon.dto.IssueCouponRequest;
import kr.hhplus.be.server.coupon.dto.IssueCouponResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupon")
public class CouponController {

    @PostMapping("/issue")
    @Tag(name = "쿠폰", description = "쿠폰과 관련된 API")
    @Operation(summary = "쿠폰 발급", description = "사용자에게 선착순으로 쿠폰을 발급합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "쿠폰 발급 성공")
    @Parameter(name = "transactionId", description = "트랜잭션 ID)", required = true, in = ParameterIn.HEADER)
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
