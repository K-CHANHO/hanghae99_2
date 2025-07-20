package kr.hhplus.be.server.balance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.balance.dto.ChargeBalanceRequest;
import kr.hhplus.be.server.balance.dto.ChargeBalanceResponse;
import kr.hhplus.be.server.balance.dto.ViewBalanceResponse;
import kr.hhplus.be.server.balance.entity.Balance;
import kr.hhplus.be.server.balance.service.BalanceService;
import kr.hhplus.be.server.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/balance")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/{userId}")
    @Tag(name = "잔액", description = "잔액과 관련된 API")
    @Operation(summary = "잔액 조회", description = "사용자의 현재 잔액을 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "잔액 조회 성공")
    @Parameter(name = "userId", description = "사용자 ID", required = true, in = ParameterIn.PATH)
    public ResponseEntity<ApiResponse<ViewBalanceResponse>> getBalance(@PathVariable String userId) {

        Balance balance = balanceService.getBalance(userId);

        ViewBalanceResponse viewBalanceResponse = new ViewBalanceResponse();
        viewBalanceResponse.setUserId(balance.getUserId());
        viewBalanceResponse.setBalance(balance.getBalance());

        ApiResponse<ViewBalanceResponse> result = new ApiResponse<>();
        result.setMessage("잔액 조회 성공");
        result.setCode(200);
        result.setData(viewBalanceResponse);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping
    @Tag(name = "잔액", description = "잔액과 관련된 API")
    @Operation(summary = "잔액 충전", description = "사용자의 잔액을 충전합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "잔액 충전 성공")
    @Parameter(name = "transactionId", description = "트랜잭션 ID", required = true, in = ParameterIn.HEADER)
    public ResponseEntity<ApiResponse<ChargeBalanceResponse>> updateBalance(@RequestBody ChargeBalanceRequest request) {

        ChargeBalanceResponse balanceResponse = new ChargeBalanceResponse();
        balanceResponse.setUserId(request.getUserId());
        balanceResponse.setNewBalance(100000 + request.getAmount()); // 예시로 100000에 충전 금액을 더함

        ApiResponse<ChargeBalanceResponse> result = new ApiResponse<>();
        result.setMessage("잔액 충전 성공");
        result.setCode(200);
        result.setData(balanceResponse);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
