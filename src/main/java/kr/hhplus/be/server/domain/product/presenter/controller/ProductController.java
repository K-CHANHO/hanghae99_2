package kr.hhplus.be.server.domain.product.presenter.controller;

import kr.hhplus.be.server.apidocs.ProductApiDocs;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.product.application.facade.dto.GetTopProductsResult;
import kr.hhplus.be.server.domain.product.application.service.dto.GetProductCommand;
import kr.hhplus.be.server.domain.product.application.service.dto.GetProductResult;
import kr.hhplus.be.server.domain.product.presenter.controller.dto.GetProductResponse;
import kr.hhplus.be.server.domain.product.presenter.controller.dto.GetTopProductResponse;
import kr.hhplus.be.server.domain.product.application.facade.ProductFacade;
import kr.hhplus.be.server.domain.product.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController implements ProductApiDocs {

    private final ProductFacade productFacade;
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<GetProductResponse>> getProduct(@PathVariable Long productId){

        GetProductCommand getProductCommand = GetProductCommand.from(productId);
        GetProductResult getProductResult = productService.getProduct(getProductCommand);

        GetProductResponse response = GetProductResponse.from(getProductResult);

        ApiResponse<GetProductResponse> result = new ApiResponse<>();
        result.setMessage("상품 조회 성공");
        result.setCode(200);
        result.setData(response);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/top")
    public ResponseEntity<ApiResponse<GetTopProductResponse>> getTopProducts() {

        GetTopProductsResult topProducts = productFacade.getTopProducts();

        GetTopProductResponse response = GetTopProductResponse.from(topProducts);

        ApiResponse<GetTopProductResponse> result = new ApiResponse<>();
        result.setMessage("인기 상품 조회 성공");
        result.setCode(200);
        result.setData(response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
