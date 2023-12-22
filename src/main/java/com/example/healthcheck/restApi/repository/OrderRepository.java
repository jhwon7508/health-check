package com.example.healthcheck.restApi.repository;

//public interface OrderRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {
//}

import com.example.healthcheck.domain.entity.Orders;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository{
    void save(Orders newOrder);
    Boolean existsByOrderCode(String s);
}