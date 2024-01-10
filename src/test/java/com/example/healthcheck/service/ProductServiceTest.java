package com.example.healthcheck.service;

import com.example.healthcheck.domain.product.dto.response.ProductResponseDTO;
import com.example.healthcheck.domain.product.repository.ProductRepository;
import com.example.healthcheck.domain.product.service.PopularProductService;
import com.example.healthcheck.domain.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;
    private PopularProductService popularProductService;

    // 각 테스트 실행 전에 실행되는 메소드
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository, popularProductService);
    }

    // 재고가 없을 경우를 테스트
    @Test
    void getProductByIdWithNoStock() {
        // 테스트 데이터
        Long productIdx = 123L;

        // stockQuantity를 0으로 설정하여 '재고 없음' 상태를 표시
        ProductResponseDTO mockProduct = ProductResponseDTO.builder()
                .productIdx(123L)
                .productCode("product123")
                .productName("Product Name")
                .price(1000L)
                .stockQuantity(0)
                .build();

        // productRepository의 getProductById 메소드 호출 시, mockProduct를 반환하도록 설정
        when(productRepository.getProductById(productIdx)).thenReturn(mockProduct);

        // 실제 서비스 메소드 호출.
        ProductResponseDTO result = productService.getProductById(productIdx);

        // 결과 검증: '재고 없음' 상태(isSoldOut = true)와 재고 수량이 0인지 확인
        assertTrue(result.getIsSoldOut());
        assertEquals(0, result.getStockQuantity());
    }

    // 재고가 있을 경우를 테스트
    @Test
    void getProductByIdWithAvailableStock() {
        // 테스트 데이터
        Long productIdx = 123L;

        // stockQuantity를 10으로 설정하여 '재고 있음' 상태를 표시
        ProductResponseDTO mockProduct = ProductResponseDTO.builder()
                .productIdx(productIdx)
                .productCode("product123")
                .productName("Product Name")
                .price(1000L)
                .stockQuantity(10)
                .build();

        // productRepository의 getProductById 메소드 호출 시, mockProduct를 반환하도록 설정
        when(productRepository.getProductById(productIdx)).thenReturn(mockProduct);

        // 실제 서비스 메소드 호출.
        ProductResponseDTO result = productService.getProductById(productIdx);

        // 결과 검증: '재고 있음' 상태(isSoldOut = false)와 재고 수량이 10인지 확인
        assertFalse(result.getIsSoldOut());
        assertEquals(10, result.getStockQuantity());
    }
}
