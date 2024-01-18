package com.example.healthcheck.domain.product.repository;

import com.example.healthcheck.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Long findPriceByProductCode(String productCode);

    int findStockQuantityByProductCode(String productCode);

    Product findByProductCode(String productCode);

    @Query("SELECT p.idx AS productIdx, p.productName AS productName, p.price AS price " +
            "FROM Product p JOIN p.orderDetail od " +
            "WHERE od.createdAt >= CURRENT_DATE - 3 AND od.createdAt < CURRENT_DATE AND od.deleteYn = FALSE " +
            "GROUP BY p.idx, p.productName, p.price " +
            "ORDER BY COUNT(od) DESC")
    List<Product> findDailyPopularProducts();

}