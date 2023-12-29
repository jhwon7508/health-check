package com.example.healthcheck.domain.product.service;

import com.example.healthcheck.domain.product.dto.response.ProductResponseDTO;
import com.example.healthcheck.domain.common.enums.ResponseCode;
import com.example.healthcheck.domain.product.entity.Product;
import com.example.healthcheck.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final PopularProductService popularProductService;

    @Cacheable(value = "products", key = "#productId")
    public ProductResponseDTO getProductById(Long productIdx) {
        try {
            popularProductService.recordAccess(productIdx); // 캐시 접근 기록
            popularProductService.registerCacheCreationTime("products::" + productIdx); // 캐시 접근 시간 기록
            Product product = productRepository.getById(productIdx);
            ProductResponseDTO result = ProductResponseDTO.builder()
                    .productIdx(productIdx)
                    .productCode(product.getProductCode())
                    .productName(product.getProductName())
                    .price(product.getPrice())
                    .stockQuantity(product.getStockQuantity())
                    .isSoldOut(product.getIsSoldOut())
                    .build();
            Boolean isSoldOut = result.getStockQuantity() <= 0;
            result.setIsSoldOut(isSoldOut);
            return result;
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.CODE_0002.toString());
        }
    }

    public Page<ProductResponseDTO> getAllProducts() {
        try {
            List<Product> findAllResult = productRepository.findAll();

            // Product 엔티티를 ProductResponseDTO로 변환
            List<ProductResponseDTO> responseDTOList = findAllResult.stream()
                    .map(product -> new ProductResponseDTO(
                            product.getIdx(),
                            product.getProductCode(),
                            product.getProductName(),
                            product.getPrice(),
                            product.getStockQuantity(),
                            product.getStockQuantity() <= 0))
                    .collect(Collectors.toList());

            // PageImpl로 페이징 처리
            Page<ProductResponseDTO> resultPage = new PageImpl<>(responseDTOList);

            return resultPage;
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.CODE_0002.toString());
        }
    }

}
