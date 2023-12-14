package com.example.healthcheck.restApi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="eCommerce API", description = "eCommerce API 목록입니다.")
@Slf4j
@RestController
@RequestMapping("/api/v1/eCommerce")
public class CommerceController {

    @Operation(summary = "잔액 충전 API")
    @PostMapping("/balance/add")
    public ResponseEntity<String> addBalance() {
        return null;
    }

    @Operation(summary = "잔액 조회 API")
    @GetMapping("/balance")
    public ResponseEntity<String> getBalance() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "상품 조회 API")
    @GetMapping("/products/{productId}")
    public ResponseEntity<String> getProduct(@PathVariable String productId) {
        return null;
    }

    @Operation(summary = "상품 목록 조회 API")
    @GetMapping("/products")
    public ResponseEntity<String> getAllProducts() {
        return null;
    }

    @Operation(summary = "주문 API")
    @PostMapping("/orders")
    public ResponseEntity<String> createOrder() {
        return null;
    }

    @Operation(summary = "결제 API")
    @PostMapping("/orders/{orderId}/payment")
    public ResponseEntity<String> processPayment(@PathVariable String orderId) {
        return null;
    }

    @Operation(summary = "인기 상품 조회 API")
    @GetMapping("/popular-products")
    public ResponseEntity<String> getPopularProducts() {
        return null;
    }

}
