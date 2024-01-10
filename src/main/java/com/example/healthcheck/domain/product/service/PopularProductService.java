package com.example.healthcheck.domain.product.service;

import com.example.healthcheck.domain.product.dto.response.PopularProductDTO;
import com.example.healthcheck.domain.product.entity.Product;
import com.example.healthcheck.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopularProductService {

    private CacheManager cacheManager;
    private final ProductRepository productRepository;

    @Cacheable(value = "popularProducts")
    public List<PopularProductDTO> getPopularProducts() {
        List<Product> popularProducts = productRepository.findDailyPopularProducts();
        return popularProducts.stream().map(product ->
                PopularProductDTO.builder()
                        .productIdx(product.getIdx())
                        .productName(product.getProductName())
                        .price(product.getPrice())
                        .build()
        ).collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 0 * * *") //캐시 삭제 매일 자정에 실행
    public void evictOldCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear(); // 캐시 제거
            }
        });
    }
}
