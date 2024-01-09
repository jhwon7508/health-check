package com.example.healthcheck.domain.order.dto.response;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private String orderCode;

    private LocalDateTime orderTime;
    //orderCode를 통해 가져온 Order 정보에 있음

    private String userName;
    //userId를 통해 가져온 User 정보에 있음

    private Long balance;
    //userId를 통해 가져온 User 정보에 있음

    private String shippingAddress;

    private Map<String, Integer> productQuantities;

    private Map<String, Long> productPrices;

    private Long totalPrice;

    // 데이터 플랫폼 센터 전송 성공 여부
    private Boolean  transmissionSuccessYn;



}
