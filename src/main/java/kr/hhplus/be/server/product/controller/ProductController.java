package kr.hhplus.be.server.product.controller;

import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.product.dto.ViewProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ViewProductResponse>> viewProduct(@PathVariable String productId){

        ViewProductResponse response = new ViewProductResponse();
        response.setProductId(productId);
        response.setProductName("항해 백엔드 9기 과정");
        response.setPrice(1500000);
        response.setStock(100);

        ApiResponse<ViewProductResponse> result = new ApiResponse<>();
        result.setMessage("상품 조회 성공");
        result.setCode(200);
        result.setData(response);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
