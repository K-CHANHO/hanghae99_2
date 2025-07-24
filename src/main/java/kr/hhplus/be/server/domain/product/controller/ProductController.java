package kr.hhplus.be.server.domain.product.controller;

import kr.hhplus.be.server.apidocs.ProductApiDocs;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.product.dto.ViewProductResponse;
import kr.hhplus.be.server.domain.product.dto.ViewTopProductResponse;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.facade.ProductFacade;
import kr.hhplus.be.server.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController implements ProductApiDocs {

    private final ProductFacade productFacade;
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ViewProductResponse>> viewProduct(@PathVariable Long productId){
        Product product = productService.getProduct(productId);

        ViewProductResponse response = new ViewProductResponse();
        response.from(product);

        ApiResponse<ViewProductResponse> result = new ApiResponse<>();
        result.setMessage("상품 조회 성공");
        result.setCode(200);
        result.setData(response);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/top")
    public ResponseEntity<ApiResponse<ViewTopProductResponse>> viewTopProducts() {

        List<Product> topProducts = productFacade.getTopProducts();

        ViewTopProductResponse response = new ViewTopProductResponse();
        response.from(topProducts);

        ApiResponse<ViewTopProductResponse> result = new ApiResponse<>();
        result.setMessage("인기 상품 조회 성공");
        result.setCode(200);
        result.setData(response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
