package com.example.healthcheck.domain.product.service;

import com.example.healthcheck.domain.common.enums.ResponseCode;
import com.example.healthcheck.domain.product.dto.response.ProductResponseDTO;
import com.example.healthcheck.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopularProductService {

    private CacheManager cacheManager;

    private final Map<String, LocalDateTime> cacheCreationTimes = new ConcurrentHashMap<>();
    private final Map<Long, Map<LocalDate, Integer>> accessCounts = new ConcurrentHashMap<>();

    public List<Long> getPopularProducts() {
        LocalDate threeDaysAgo = LocalDate.now().minusDays(3); // 3일 전 날짜 계산

        return accessCounts.entrySet().stream() // 상품별 접근 횟수 데이터 스트림 생성
                .flatMap(entry ->
                        entry.getValue().entrySet().stream() // 날짜별 접근 횟수에 대한 스트림 생성
                                .filter(dateCountEntry ->
                                        !dateCountEntry.getKey().isBefore(threeDaysAgo)) // 3일 전보다 이후의 데이터만 필터링
                                .map(dateCountEntry ->
                                        new AbstractMap.SimpleEntry<>(entry.getKey(), dateCountEntry.getValue())
                                ) // 상품 ID와 해당 날짜의 접근 횟수를 쌍으로 매핑
                )
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue))) // 상품별로 접근 횟수 합산
                .entrySet().stream() // 결과 맵의 항목에 대한 스트림 생성
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed()) // 접근 횟수가 많은 순으로 정렬
                .limit(5) // 상위 5개 항목 제한
                .map(Map.Entry::getKey) // 상품 ID만 추출
                .collect(Collectors.toList()); // 결과 리스트로 변환
    }

    public void recordAccess(Long productId) {
        LocalDate today = LocalDate.now();
        accessCounts.computeIfAbsent(productId, k -> new ConcurrentHashMap<>()).merge(today, 1, Integer::sum);
    }

    public void registerCacheCreationTime(String cacheName) {
        cacheCreationTimes.put(cacheName, LocalDateTime.now());
    }

    // 생성일로부터 4일이 경과되면, 삭제 대상 oldCache로 분류
    public boolean isCacheOld(String cacheName) {
        LocalDateTime creationTime = cacheCreationTimes.get(cacheName);
        if (creationTime == null) {
            return false;
        }
        return creationTime.plusDays(4).withHour(0).withMinute(0).withSecond(0).withNano(0).isBefore(LocalDateTime.now());
    }

    @Scheduled(cron = "0 0 0 * * *") //캐시 삭제 매일 자정에 실행
    public void evictOldCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null && isCacheOld(cacheName)) {
                cache.clear(); // 캐시 제거
            }
        });
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Map<Long, Map<LocalDate, Integer>> getAccessCounts() {
        return accessCounts;
    }

    public void setAccessCounts(Map<Long, Map<LocalDate, Integer>> accessCounts) {
        this.accessCounts.clear();
        this.accessCounts.putAll(accessCounts);
    }
}
