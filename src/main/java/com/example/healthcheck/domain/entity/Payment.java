package com.example.healthcheck.domain.entity;

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
@Table(name = "Payment")
@Where(clause = "deleteYn = 0")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull
    private String paymentCode;

    @NotNull
    private String paymentType;

    @NotNull
    private String paymentMethod;

    @NotNull
    private Long paymentAmount;

    @NotNull
    private Boolean deleteYn;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private String createdBy;
}
