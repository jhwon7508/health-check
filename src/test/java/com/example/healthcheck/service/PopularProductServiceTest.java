package com.example.healthcheck.service;

import com.example.healthcheck.domain.product.dto.response.PopularProductDTO;
import com.example.healthcheck.domain.product.service.PopularProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PopularProductServiceTest {

    private PopularProductService popularProductService;

    @Mock
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        // Mockito 초기화
        MockitoAnnotations.initMocks(this);
//        popularProductService = new PopularProductService();
    }

    @Test
    void getPopularProductsReturnsTop5Products() {
        // Arrange
        Map<Long, Map<LocalDate, Integer>> sampleAccessCounts = new ConcurrentHashMap<>();
        for (long i = 1; i <= 10; i++) {
            Map<LocalDate, Integer> dailyCounts = new ConcurrentHashMap<>();
            dailyCounts.put(LocalDate.now(), (int) i * 10); // 상품별 접근 횟수 설정
            sampleAccessCounts.put(i, dailyCounts);
        }


        // 캐시 관련 가짜 객체 생성
//        when(cacheManager.getCache("cache1")).thenReturn(createCache("cache1"));
//        when(cacheManager.getCache("cache2")).thenReturn(createCache("cache2"));
//        when(cacheManager.getCache("cache3")).thenReturn(createCache("cache3"));

        // Act
        List<PopularProductDTO> popularProducts = popularProductService.getPopularProducts();

        // Assert
        assertThat(popularProducts).hasSize(5);
//        assertThat(popularProducts).containsExactly(10L, 9L, 8L, 7L, 6L);
    }


    @Test
    void recordAccessIncrementsAccessCount() {
        // Arrange
        Long productId = 1L;

        // Act
//        popularProductService.recordAccess(productId);
//        popularProductService.recordAccess(productId);
//        popularProductService.recordAccess(productId);

        // Assert
//        Map<LocalDate, Integer> accessCount = popularProductService.getAccessCounts().get(productId);
//        assertThat(accessCount).containsOnlyKeys(LocalDate.now());
//        assertThat(accessCount).containsValues(3);
    }

    @Test
    void isCacheOldReturnsTrueForOldCache() {
        // Arrange
//        popularProductService.registerCacheCreationTime("oldCache");

        // Act
//        boolean result = popularProductService.isCacheOld("oldCache");

        // Assert
//        assertThat(result).isTrue();
    }

    @Test
    void isCacheOldReturnsFalseForRecentCache() {
        // Arrange
//        popularProductService.registerCacheCreationTime("recentCache");

        // Act
//        boolean result = popularProductService.isCacheOld("recentCache");

        // Assert
//        assertThat(result).isFalse();
    }

    @Test
    void evictOldCachesClearsOldCache() {
        // Arrange
        Cache oldCache = createCache("oldCache");
        Cache recentCache = createCache("recentCache");
        when(cacheManager.getCacheNames()).thenReturn(List.of("oldCache", "recentCache"));
        when(cacheManager.getCache("oldCache")).thenReturn(oldCache);
        when(cacheManager.getCache("recentCache")).thenReturn(recentCache);
//        popularProductService.registerCacheCreationTime("oldCache");
//        popularProductService.registerCacheCreationTime("recentCache");

        // Act
        popularProductService.evictOldCaches();

        // Assert
        verify(oldCache, times(1)).clear();
        verify(recentCache, never()).clear();
    }

    private Cache createCache(String cacheName) {
        Cache cache = mock(Cache.class);
        when(cache.getName()).thenReturn(cacheName);
        return cache;
    }
}
