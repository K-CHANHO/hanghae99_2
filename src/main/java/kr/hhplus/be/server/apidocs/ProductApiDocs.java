package kr.hhplus.be.server.apidocs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.product.dto.ViewProductResponse;
import kr.hhplus.be.server.domain.product.dto.ViewTopProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductApiDocs {

    @Tag(name = "상품", description = "상품과 관련된 API")
    @Operation(summary = "상품 조회", description = "상품 ID로 상품 정보를 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "상품 조회 성공")
    @Parameter(name = "productId", description = "상품 ID", required = true, in = ParameterIn.PATH)
    public ResponseEntity<ApiResponse<ViewProductResponse>> viewProduct(@PathVariable String productId);

    @Tag(name = "상품", description = "상품과 관련된 API")
    @Operation(summary = "인기 상품 조회", description = "인기 상품 목록 5개를 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인기 상품 조회 성공")
    public ResponseEntity<ApiResponse<ViewTopProductResponse>> viewTopProducts();

}
