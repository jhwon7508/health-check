package com.example.healthcheck.restApi.service;

import com.example.healthcheck.domain.dto.response.ProductResponseDTO;
import com.example.healthcheck.enums.ResponseCode;
import com.example.healthcheck.restApi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDTO getProductById(String productId) {
        try {
            ProductResponseDTO result = productRepository.getProductById(productId);
            Boolean isSoldOut = result.getStockQuantity() <= 0;
            result.setIsSoldOut(isSoldOut);
            return result;
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.CODE_0002.toString());
        }
    }

    public Page<ProductResponseDTO> getAllProducts() {
        try {
            Page<ProductResponseDTO> result = productRepository.getAllProducts();
            return result;
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.CODE_0002.toString());
        }

    }
}
