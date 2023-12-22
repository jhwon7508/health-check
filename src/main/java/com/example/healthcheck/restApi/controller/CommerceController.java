package com.example.healthcheck.restApi.controller;

import com.example.healthcheck.domain.dto.request.BalanceDTO;
import com.example.healthcheck.domain.dto.request.OrderDTO;
import com.example.healthcheck.domain.dto.response.OrderResponseDTO;
import com.example.healthcheck.domain.dto.response.ProductResponseDTO;
import com.example.healthcheck.domain.dto.response.BalanceResponseDTO;
import com.example.healthcheck.enums.ResponseCode;
import com.example.healthcheck.restApi.service.BalanceService;
import com.example.healthcheck.restApi.service.OrderService;
import com.example.healthcheck.restApi.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

//@Tag(name="eCommerce API", description = "eCommerce API 목록입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommerceController {

    private final BalanceService balanceService;
    private final OrderService orderService;
    private final ProductService productService;

    //    @Operation(summary = "잔액 충전 API")
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseCode.CODE_0002);
        }
    }

    //    @Operation(summary = "상품 조회 API")
    @GetMapping("/product/detail/{productId}")
    public ResponseEntity<?> getProductById(@Valid @PathVariable String productId) {
        try {
            ProductResponseDTO product = productService.getProductById(productId);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ResponseCode.CODE_2001);
        }
    }

    //    @Operation(summary = "상품 목록 조회 API")
    @GetMapping("/product/all")
    public ResponseEntity<?> getAllProducts() {
        Page<ProductResponseDTO> productList = productService.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    //    @Operation(summary = "주문 API")
    @PostMapping("/order/request")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody OrderDTO orderRequest) {
        try {
            OrderResponseDTO result = orderService.placeOrder(orderRequest);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ResponseCode.CODE_1001);
        }
    }

    //    @Operation(summary = "결제 API")
    @PostMapping("/order/{orderId}/payment")
    public ResponseEntity<?> processPayment(@Valid @PathVariable String orderId) {

        return null;
    }

    //    @Operation(summary = "인기 상품 조회 API")
    @GetMapping("/statistcs/popular-products")
    public ResponseEntity<?> getPopularProducts() {
        return null;
    }

}
