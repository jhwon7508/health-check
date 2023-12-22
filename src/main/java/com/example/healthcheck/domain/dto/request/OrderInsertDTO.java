package com.example.healthcheck.domain.dto.request;

import com.example.healthcheck.domain.entity.Orders;
import com.example.healthcheck.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInsertDTO {

    private User user; // 사용자
    private String orderCode; // 주문 코드
    private String orderStatus; // 주문 상태
    private String shippingAddress; // 배송 주소
    private Map<String, Integer> productQuantities; // 상품 코드와 수량
    private Boolean deleteYn; // 삭제 여부
    private LocalDateTime createdAt; // 생성 시간
    private String createdBy; // 생성자

    public Orders convertToEntity(User user) {

        return Orders.builder()
                .user(user)
                .orderCode(this.orderCode)
                .shippingAddress(this.shippingAddress)
                .orderStatus("PENDING") // 초기 상태 설정
                .deleteYn(false)
                .createdAt(LocalDateTime.now())
                .createdBy(user.getUserId())
                .build();
    }

}
