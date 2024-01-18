package com.example.healthcheck.domain.product.repository.impl;

import com.example.healthcheck.domain.product.dto.response.PopularProductDTO;
import com.example.healthcheck.domain.product.entity.Product;
import com.example.healthcheck.domain.product.repository.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

//@RequiredArgsConstructor
//public class ProductRepositoryImpl implements ProductRepositoryCustom {
//    private final JPAQueryFactory queryFactory;

//    @Override
//    public List<Product> findDailyPopularProducts() {
//        QProduct product = QProduct.product;
//        QOrderDetail orderDetail = QOrderDetail.orderDetail;
//
//        LocalDate threeDaysAgo = LocalDate.now().minusDays(3);
//
//        return queryFactory
//                .select(Projections.constructor(PopularProductDTO.class,
//                        product.idx,
//                        product.productName,
//                        product.price))
//                .from(orderDetail)
//                .join(orderDetail.product, product)
//                .where(orderDetail.createdAt.between(threeDaysAgo.atStartOfDay(), LocalDate.now().atStartOfDay())
//                        .and(orderDetail.deleteYn.eq(false))
//                        .and(product.deleteYn.eq(false)))
//                .groupBy(product.idx)
//                .orderBy(orderDetail.count().desc())
//                .fetch();
//    }
//}