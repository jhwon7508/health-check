package com.example.healthcheck.domain.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "OrderDetail")
@Where(clause = "deleteYn = 0")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @OneToMany(mappedBy = "orderDetail")
    List<Product> productList = new ArrayList<>();

    @NotNull
    private String orderDetailCode;

    @NotNull
    private Integer orderQuantity;

    @NotNull
    private Long orderPrice;

    @NotNull
    private Boolean deleteYn;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private String createdBy;
}
