package com.example.healthcheck.service;

import com.example.healthcheck.domain.product.dto.request.ProductRequestDto;
import com.example.healthcheck.domain.product.dto.response.ProductResponseDTO;
import com.example.healthcheck.domain.product.entity.Product;
import com.example.healthcheck.domain.product.repository.ProductRepository;
import com.example.healthcheck.domain.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);
    }

    @Test
    void getProductByIdWithNoStock() {
        // 테스트 데이터
        Long productIdx = 123L;

        // stockQuantity를 0으로 설정하여 '재고 없음' 상태를 표시
        Product mockProduct = new Product();
        mockProduct.setIdx(productIdx);
        mockProduct.setProductCode("product123");
        mockProduct.setProductName("Product Name");
        mockProduct.setPrice(1000L);
        mockProduct.setStockQuantity(0);

        // productRepository의 getById 메소드 호출 시, mockProduct를 반환하도록 설정
        when(productRepository.getById(productIdx)).thenReturn(mockProduct);

        // 실제 서비스 메소드 호출.
        ProductResponseDTO result = productService.getProductById(productIdx);

        // 결과 검증: '재고 없음' 상태(isSoldOut = true)와 재고 수량이 0인지 확인
        assertEquals(productIdx, result.getProductIdx());
        assertTrue(result.getIsSoldOut());
        assertEquals(0, result.getStockQuantity());
    }

    @Test
    void getProductByIdWithAvailableStock() {
        // 테스트 데이터
        Long productIdx = 123L;

        // stockQuantity를 10으로 설정하여 '재고 있음' 상태를 표시
        Product mockProduct = new Product();
        mockProduct.setIdx(productIdx);
        mockProduct.setProductCode("product123");
        mockProduct.setProductName("Product Name");
        mockProduct.setPrice(1000L);
        mockProduct.setStockQuantity(10);

        // productRepository의 getById 메소드 호출 시, mockProduct를 반환하도록 설정
        when(productRepository.getById(productIdx)).thenReturn(mockProduct);

        // 실제 서비스 메소드 호출.
        ProductResponseDTO result = productService.getProductById(productIdx);

        // 결과 검증: '재고 있음' 상태(isSoldOut = false)와 재고 수량이 10인지 확인
        assertEquals(productIdx, result.getProductIdx());
        assertFalse(result.getIsSoldOut());
        assertEquals(10, result.getStockQuantity());
    }

    @Test
    void getAllProducts() {
        // 테스트 데이터
        Pageable pageable = Pageable.unpaged();
        Product mockProduct = new Product();
        mockProduct.setIdx(1L);
        mockProduct.setProductCode("product123");
        mockProduct.setProductName("Product Name");
        mockProduct.setPrice(1000L);
        mockProduct.setStockQuantity(10);

        // productRepository의 findAll 메소드 호출 시, Page 객체를 반환하도록 설정
        when(productRepository.findAll(pageable)).thenReturn(Page.empty());

        // 실제 서비스 메소드 호출.
        Page<ProductResponseDTO> result = productService.getAllProducts(pageable);

        // 결과 검증: 페이지가 비어있는지 확인
        assertTrue(result.isEmpty());
    }

    @Test
    void addProduct() {
        // 테스트 데이터
        ProductRequestDto productDto = new ProductRequestDto();

        // productRepository의 save 메소드 호출 시, productDto를 반환하도록 설정
        when(productRepository.save(any())).thenReturn(new Product());

        // 실제 서비스 메소드 호출.
        assertDoesNotThrow(() -> productService.addProduct(productDto));

        // verify를 사용하여 productRepository의 save 메소드가 호출되었는지 확인
        verify(productRepository, times(1)).save(any());
    }

    @Test
    void addProductWithException() {
        // 테스트 데이터
        ProductRequestDto productDto = new ProductRequestDto();

        // productRepository의 save 메소드 호출 시 예외를 던지도록 설정
        when(productRepository.save(any())).thenThrow(new RuntimeException("Database error"));

        // 예외 발생을 확인하는 테스트
        assertThrows(RuntimeException.class, () -> productService.addProduct(productDto));
    }
}
