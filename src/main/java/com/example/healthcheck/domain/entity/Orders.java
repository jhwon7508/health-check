package com.example.healthcheck.domain.entity;

import com.example.healthcheck.domain.dto.request.OrderInsertDTO;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Data
@SuperBuilder
@Table(name = "Orders")
@Where(clause = "deleteYn = 0")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "orders")
    List<OrderDetail> orderDetailList = new ArrayList<>();

    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    private PopularProduct popularProduct;

    @NotNull
    private String orderCode;

    @NotNull
    private String orderStatus;

    @NotNull
    private String shippingAddress;

    @NotNull
    private Boolean deleteYn;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private String createdBy;


}
