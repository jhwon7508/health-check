package com.example.healthcheck.domain.user.entity;

import com.example.healthcheck.domain.order.entity.Orders;
import com.example.healthcheck.domain.user.dto.request.BalanceDTO;
import com.example.healthcheck.domain.user.dto.response.BalanceResponseDTO;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "User")
@Where(clause = "deleteYn = 0")
@DynamicInsert
@DynamicUpdate
@RequiredArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @NotNull
    private String userId;

    @NotNull
    private String password;

    @NotNull
    private String userName;

    @NotNull
    private String userEmail;

    @NotNull
    private String address;

    @NotNull
    private Long balance;

    @OneToMany(mappedBy = "user")
    List<Orders> ordersList = new ArrayList<>();

    @NotNull
    private Boolean deleteYn;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public User(String user, Long amount) {
        this.userId = user;
        this.balance = amount;
    }
}
