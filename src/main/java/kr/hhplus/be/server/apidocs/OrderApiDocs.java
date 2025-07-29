package kr.hhplus.be.server.apidocs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.order.presenter.controller.dto.OrderRequest;
import kr.hhplus.be.server.domain.order.presenter.controller.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderApiDocs {

    @Tag(name = "주문", description = "주문과 관련된 API")
    @Operation(summary = "주문 생성", description = "사용자가 상품을 주문합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주문 생성 성공")
    ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestBody OrderRequest request);
}
