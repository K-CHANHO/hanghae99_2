package kr.hhplus.be.server.apidocs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.balance.controller.dto.ChargeBalanceRequest;
import kr.hhplus.be.server.domain.balance.controller.dto.ChargeBalanceResponse;
import kr.hhplus.be.server.domain.balance.controller.dto.ViewBalanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface BalanceApiDocs {

    @Tag(name = "잔액", description = "잔액과 관련된 API")
    @Operation(summary = "잔액 조회", description = "사용자의 현재 잔액을 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "잔액 조회 성공")
    @Parameter(name = "userId", description = "사용자 ID", required = true, in = ParameterIn.PATH)
    ResponseEntity<ApiResponse<ViewBalanceResponse>> getBalance(@PathVariable String userId);


    @Tag(name = "잔액", description = "잔액과 관련된 API")
    @Operation(summary = "잔액 충전", description = "사용자의 잔액을 충전합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "잔액 충전 성공")
    @Parameter(name = "transactionId", description = "트랜잭션 ID", required = true, in = ParameterIn.HEADER)
    ResponseEntity<ApiResponse<ChargeBalanceResponse>> updateBalance(@RequestBody ChargeBalanceRequest request);


}
