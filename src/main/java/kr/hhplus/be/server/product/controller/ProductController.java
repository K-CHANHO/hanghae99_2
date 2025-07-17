package kr.hhplus.be.server.product.controller;

import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.product.dto.ViewProductResponse;
import kr.hhplus.be.server.product.dto.ViewTopProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/top")
    public ResponseEntity<ApiResponse<ViewTopProductResponse>> viewTopProducts() {
        ViewTopProductResponse response = new ViewTopProductResponse();

        // 예시 데이터 설정
        List<ViewTopProductResponse.Product> topProducts = new ArrayList<>();
        ViewTopProductResponse.Product product1 = new ViewTopProductResponse.Product();
        product1.setProductId("product1");
        product1.setProductName("항해 백엔드 9기 과정");
        product1.setSoldCount(100);
        product1.setPrice(1500000);

        ViewTopProductResponse.Product product2 = new ViewTopProductResponse.Product();
        product2.setProductId("product2");
        product2.setProductName("항해 프론트엔드 9기 과정");
        product2.setSoldCount(50);
        product2.setPrice(1200000);

        ViewTopProductResponse.Product product3 = new ViewTopProductResponse.Product();
        product3.setProductId("product3");
        product3.setProductName("항해 데브옵스 9기 과정");
        product3.setSoldCount(70);
        product3.setPrice(1600000);

        ViewTopProductResponse.Product product4 = new ViewTopProductResponse.Product();
        product4.setProductId("product4");
        product4.setProductName("항해 풀스택 9기 과정");
        product4.setSoldCount(20);
        product4.setPrice(2000000);

        ViewTopProductResponse.Product product5 = new ViewTopProductResponse.Product();
        product5.setProductId("product5");
        product5.setProductName("항해 학습메이트 9기 과정");
        product5.setSoldCount(5);
        product5.setPrice(1000000);

        topProducts.add(product1);
        topProducts.add(product2);
        topProducts.add(product3);
        topProducts.add(product4);
        topProducts.add(product5);

        response.setTopProducts(topProducts);

        ApiResponse<ViewTopProductResponse> result = new ApiResponse<>();
        result.setMessage("인기 상품 조회 성공");
        result.setCode(200);
        result.setData(response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
