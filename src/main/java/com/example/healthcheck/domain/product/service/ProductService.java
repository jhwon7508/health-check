package com.example.healthcheck.domain.product.service;

import com.example.healthcheck.domain.product.dto.request.ProductRequestDto;
import com.example.healthcheck.domain.product.dto.response.ProductResponseDTO;
import com.example.healthcheck.domain.common.enums.ResponseCode;
import com.example.healthcheck.domain.product.entity.Product;
import com.example.healthcheck.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public ProductResponseDTO getProductById(Long productIdx) {
        try {
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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.DATABASE_ERROR.toString());
        }
    }

    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> new ProductResponseDTO(
                product.getIdx(),
                product.getProductCode(),
                product.getProductName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getStockQuantity() <= 0));
    }

    public void addProducts(List<ProductRequestDto> productList) {
        List<Product> products = productList.stream()
                .map(productDto -> productDto.convertToEntity(productDto)) // convertToEntity를 람다 표현식으로 호출
                .collect(Collectors.toList());
        productRepository.saveAll(products);
    }

    public void addProduct(ProductRequestDto productDto) {
        Product product = productDto.convertToEntity(productDto);
        productRepository.save(product);
    }

}
