package kr.hhplus.be.server.domain.balance.presenter.controller;

import kr.hhplus.be.server.apidocs.BalanceApiDocs;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.balance.presenter.controller.dto.ChargeBalanceRequest;
import kr.hhplus.be.server.domain.balance.presenter.controller.dto.ChargeBalanceResponse;
import kr.hhplus.be.server.domain.balance.presenter.controller.dto.GetBalanceResponse;
import kr.hhplus.be.server.domain.balance.application.service.BalanceService;
import kr.hhplus.be.server.domain.balance.application.service.dto.ChargeBalanceCommand;
import kr.hhplus.be.server.domain.balance.application.service.dto.ChargeBalanceResult;
import kr.hhplus.be.server.domain.balance.application.service.dto.GetBalanceCommand;
import kr.hhplus.be.server.domain.balance.application.service.dto.GetBalanceResult;
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
    public ResponseEntity<ApiResponse<GetBalanceResponse>> getBalance(@PathVariable String userId) {

        GetBalanceCommand getBalanceCommand = GetBalanceCommand.from(userId);

        GetBalanceResult getBalanceResult = balanceService.getBalance(getBalanceCommand);
        GetBalanceResponse getBalanceResponse = GetBalanceResponse.from(getBalanceResult);

        ApiResponse<GetBalanceResponse> result = new ApiResponse<>();
        result.setMessage("잔액 조회 성공");
        result.setCode(200);
        result.setData(getBalanceResponse);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<ChargeBalanceResponse>> updateBalance(@RequestBody ChargeBalanceRequest request) {

        ChargeBalanceCommand chargeBalanceCommand = ChargeBalanceCommand.from(request);

        ChargeBalanceResult chargeBalanceResult = balanceService.chargeBalance(chargeBalanceCommand);
        ChargeBalanceResponse chargeBalanceResponse = ChargeBalanceResponse.from(chargeBalanceResult);

        ApiResponse<ChargeBalanceResponse> result = new ApiResponse<>();
        result.setMessage("잔액 충전 성공");
        result.setCode(200);
        result.setData(chargeBalanceResponse);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
