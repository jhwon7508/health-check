package com.example.healthcheck.domain.order.repository;

import com.example.healthcheck.domain.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {
    Orders findByOrderCode(String orderCode);

    Orders findByOrderId(String orderId);
}

//import com.example.healthcheck.domain.order.entity.Orders;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface OrderRepository{
//    void save(Orders newOrder);
//    Boolean existsByOrderCode(String s);
//}