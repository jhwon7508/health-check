package com.example.healthcheck.domain.dto.response;

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

    private String orderId;

    private LocalDateTime orderTime;
    //orderId를 통해 가져온 Order 정보에 있음

    private String userName;
    //userId를 통해 가져온 User 정보에 있음

    private Long balance;
    //userId를 통해 가져온 User 정보에 있음

    private String shippingAddress;

    private Map<String, Integer> productQuantities;

    private Map<String, Long> productPrices;

    private Long totalPrice;

    private Boolean paymentSuccessYn;

}
