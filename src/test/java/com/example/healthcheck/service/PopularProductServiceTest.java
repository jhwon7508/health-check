package com.example.healthcheck.service;

import com.example.healthcheck.domain.product.dto.response.PopularProductDTO;
import com.example.healthcheck.domain.product.entity.Product;
import com.example.healthcheck.domain.product.repository.ProductRepository;
import com.example.healthcheck.domain.product.service.PopularProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PopularProductServiceTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private PopularProductService popularProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPopularProducts() {
        // 테스트 데이터
        List<Product> mockPopularProducts = Collections.singletonList(new Product());

        // productRepository의 findDailyPopularProducts 메소드 호출 시, mockPopularProducts를 반환하도록 설정
        when(productRepository.findDailyPopularProducts()).thenReturn(mockPopularProducts);

        // 실제 서비스 메소드 호출.
        List<PopularProductDTO> result = popularProductService.getPopularProducts();

        // 결과 검증: 서비스 메소드가 정상적으로 데이터를 반환하는지 확인
        assertNotNull(result);
        assertEquals(mockPopularProducts.size(), result.size());
    }

    @Test
    void evictOldCaches() {
        // 테스트 데이터
        Cache mockCache = mock(Cache.class);

        // cacheManager의 getCacheNames 메소드 호출 시, cacheName을 반환하도록 설정
        when(cacheManager.getCacheNames()).thenReturn(Collections.singletonList("popularProducts"));

        // cacheManager의 getCache 메소드 호출 시, mockCache를 반환하도록 설정
        when(cacheManager.getCache("popularProducts")).thenReturn(mockCache);

        // 실제 서비스 메소드 호출.
        assertDoesNotThrow(() -> popularProductService.evictOldCaches());

        // verify를 사용하여 cacheManager의 clear 메소드가 호출되었는지 확인
        verify(mockCache, times(1)).clear();
    }

    @Test
    void cacheManagerReturnsNull() {
        // 테스트 데이터
        String cacheName = "popularProducts";

        // cacheManager의 getCacheNames 메소드 호출 시, cacheName을 반환하도록 설정
        when(cacheManager.getCacheNames()).thenReturn(Collections.singletonList(cacheName));

        // cacheManager의 getCache 메소드 호출 시 null 반환
        when(cacheManager.getCache(cacheName)).thenReturn(null);

        // 실제 서비스 메소드 호출.
        assertDoesNotThrow(() -> popularProductService.evictOldCaches());

        // verify를 사용하여 cacheManager의 clear 메소드가 호출되지 않았는지 확인
        verifyNoInteractions(cacheManager);
    }
}
