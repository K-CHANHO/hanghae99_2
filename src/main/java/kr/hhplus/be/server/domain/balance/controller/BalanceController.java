package kr.hhplus.be.server.domain.balance.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.apidocs.BalanceApiDocs;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.balance.dto.*;
import kr.hhplus.be.server.domain.balance.entity.Balance;
import kr.hhplus.be.server.domain.balance.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/balance")
@RequiredArgsConstructor
public class BalanceController implements BalanceApiDocs {

    private final BalanceService balanceService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<ViewBalanceResponse>> getBalance(@PathVariable String userId) {

        ViewBalanceServiceRequestDto viewBalanceServiceRequestDto = new ViewBalanceServiceRequestDto(userId);

        ViewBalanceServiceResponseDto serviceResponseDto = balanceService.getBalance(viewBalanceServiceRequestDto);
        ViewBalanceResponse viewBalanceResponse = new ViewBalanceResponse(serviceResponseDto);

        ApiResponse<ViewBalanceResponse> result = new ApiResponse<>();
        result.setMessage("잔액 조회 성공");
        result.setCode(200);
        result.setData(viewBalanceResponse);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping
    @Tag(name = "잔액", description = "잔액과 관련된 API")
    public ResponseEntity<ApiResponse<ChargeBalanceResponse>> updateBalance(@RequestBody ChargeBalanceRequest request) {

        Balance balance = balanceService.chargeBalance(request.getUserId(), request.getAmount());
        ChargeBalanceResponse chargeBalanceResponse = new ChargeBalanceResponse();
        chargeBalanceResponse.from(balance);


        ApiResponse<ChargeBalanceResponse> result = new ApiResponse<>();
        result.setMessage("잔액 충전 성공");
        result.setCode(200);
        result.setData(chargeBalanceResponse);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
