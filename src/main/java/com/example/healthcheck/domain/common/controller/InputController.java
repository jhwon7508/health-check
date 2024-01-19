package com.example.healthcheck.domain.common.controller;

import com.example.healthcheck.domain.common.enums.ResponseCode;
import com.example.healthcheck.domain.order.dto.request.OrderDTO;
import com.example.healthcheck.domain.order.dto.response.OrderResponseDTO;
import com.example.healthcheck.domain.order.service.OrderService;
import com.example.healthcheck.domain.product.dto.request.ProductRequestDto;
import com.example.healthcheck.domain.product.dto.response.PopularProductDTO;
import com.example.healthcheck.domain.product.dto.response.ProductResponseDTO;
import com.example.healthcheck.domain.product.service.PopularProductService;
import com.example.healthcheck.domain.product.service.ProductService;
import com.example.healthcheck.domain.user.dto.request.BalanceDTO;
import com.example.healthcheck.domain.user.dto.response.BalanceResponseDTO;
import com.example.healthcheck.domain.user.service.BalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
public class InputController {

    private final BalanceService balanceService;
    private final OrderService orderService;
    private final ProductService productService;
    private final PopularProductService popularProductService;


    //Product
    @PostMapping("/product/addMultiple")
    public ResponseEntity<String> addProducts(@RequestBody List<ProductRequestDto> productList) {
        productService.addProducts(productList);
        return ResponseEntity.ok("Products added successfully");
    }

    @PostMapping("/product/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequestDto product) {
        productService.addProduct(product);
        return ResponseEntity.ok("Product added successfully");
    }
}
