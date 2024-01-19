package com.example.healthcheck.domain.product.entity;

import com.example.healthcheck.domain.order.entity.OrderDetail;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Product")
@Where(clause = "deleteYn = 0")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToOne(mappedBy = "product")
    private OrderDetail orderDetail;

    @NotNull
    private String productCode;

    @NotNull
    private String productName;

    @NotNull
    private Long price;

    @NotNull
    private Integer stockQuantity;

    @NotNull
    private String category;

    @Lob
    private String description;

    @NotNull
    private Boolean isSoldOut;

    @NotNull
    private Boolean deleteYn;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private String createdBy;

    @NotNull
    private LocalDateTime updatedAt;

    @NotNull
    private String updatedBy;
}
