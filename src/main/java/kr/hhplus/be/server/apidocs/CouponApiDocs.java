package kr.hhplus.be.server.apidocs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.coupon.controller.dto.IssueCouponRequest;
import kr.hhplus.be.server.domain.coupon.controller.dto.IssueCouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface CouponApiDocs {

    @Tag(name = "쿠폰", description = "쿠폰과 관련된 API")
    @Operation(summary = "쿠폰 발급", description = "사용자에게 선착순으로 쿠폰을 발급합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "쿠폰 발급 성공")
    @Parameter(name = "transactionId", description = "트랜잭션 ID)", required = true, in = ParameterIn.HEADER)
    ResponseEntity<ApiResponse<IssueCouponResponse>> issueCoupon(@RequestBody IssueCouponRequest request);




}
