package com.example.healthcheck.domain.order.entity;

import com.example.healthcheck.domain.user.entity.User;
import com.sun.istack.NotNull;
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
