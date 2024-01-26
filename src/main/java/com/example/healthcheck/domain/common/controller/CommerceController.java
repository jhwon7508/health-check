package com.example.healthcheck.domain.common.controller;

import com.example.healthcheck.domain.product.dto.response.PopularProductDTO;
import com.example.healthcheck.domain.product.service.PopularProductService;
import com.example.healthcheck.domain.user.dto.request.BalanceDTO;
import com.example.healthcheck.domain.order.dto.request.OrderDTO;
import com.example.healthcheck.domain.user.dto.response.BalanceResponseDTO;
import com.example.healthcheck.domain.order.dto.response.OrderResponseDTO;
import com.example.healthcheck.domain.product.dto.response.ProductResponseDTO;
import com.example.healthcheck.domain.order.service.OrderService;
import com.example.healthcheck.domain.product.service.ProductService;
import com.example.healthcheck.domain.user.service.BalanceService;
import com.example.healthcheck.domain.common.enums.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;

//@Tag(name="eCommerce API", description = "eCommerce API 목록입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommerceController {

    private final BalanceService balanceService;
    private final OrderService orderService;
    private final ProductService productService;
    private final PopularProductService popularProductService;

    //        @Operation(summary = "잔액 충전 API")
    @PostMapping("/balance/add")
    public ResponseEntity<?> addBalance(@Valid @RequestBody BalanceDTO balanceDTO) {
        BalanceResponseDTO result = balanceService.chargeBalance(balanceDTO);
        return ResponseEntity.ok(result);
    }

    //    @Operation(summary = "잔액 조회 API")
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@Valid @PathVariable String userId) {
        try {
            BalanceResponseDTO balance = balanceService.checkBalance(userId);
            return ResponseEntity.ok(balance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseCode.DATABASE_ERROR);
        }
    }

    //    @Operation(summary = "상품 조회 API")
    @GetMapping("/product/detail/{productId}")
    public ResponseEntity<?> getProductById(@Valid @PathVariable Long productIdx) {
        try {
            ProductResponseDTO product = productService.getProductById(productIdx);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ResponseCode.INVALID_PRODUCT);
        }
    }

    //    @Operation(summary = "상품 목록 조회 API")
    @GetMapping("/product/all")
    public ResponseEntity<?> getAllProducts(Pageable pageable) {
        Page<ProductResponseDTO> productList = productService.getAllProducts(pageable);
        return ResponseEntity.ok(productList);
    }

    //    @Operation(summary = "주문 API")
    @PostMapping("/order/request")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody OrderDTO orderRequest) {
        try {
            OrderResponseDTO result = orderService.placeOrder(orderRequest);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ResponseCode.INVALID_ORDER);
        } catch (ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseCode.SYSTEM_ERROR);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseCode.SYSTEM_ERROR);
        }
    }

    //    @Operation(summary = "인기 상품 조회 API")
    @GetMapping("/statistcs/popular-products")
    public ResponseEntity<?> getPopularProducts() {
        try {
            List<PopularProductDTO> popularProducts = popularProductService.getPopularProducts();
            return ResponseEntity.ok(popularProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ResponseCode.SYSTEM_ERROR);
        }
    }

}
