package com.example.healthcheck.domain.product.repository;

import com.example.healthcheck.domain.product.dto.response.ProductResponseDTO;
import com.example.healthcheck.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Long findPriceByProductCode(String productCode);

    int findStockQuantityByProductCode(String productCode);

    Product findByProductCode(String productCode);

//@Repository
//public interface ProductRepository {

    //    ProductResponseDTO getProductById(Long productId);

//    Integer getStockQuantity(String productId);
//    Boolean isProductSoldOut(String productId);

//    void setupProductStock(String productId, int stock);
}