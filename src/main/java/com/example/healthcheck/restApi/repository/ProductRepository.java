package com.example.healthcheck.restApi.repository;

import com.example.healthcheck.domain.dto.request.OrderDTO;
import com.example.healthcheck.domain.dto.response.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

//public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
//}

@Repository
public interface ProductRepository {

    ProductResponseDTO getProductById(String productId);

    Page<ProductResponseDTO> getAllProducts();

    Long getProductPrice(String productId);

    Integer getStockQuantity(String productId);

    Boolean isProductSoldOut(String productId);

    Long findPriceByProductId(String productId);

    void deductStock(String eq, int eq1);
}