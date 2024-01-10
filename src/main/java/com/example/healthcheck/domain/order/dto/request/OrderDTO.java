package com.example.healthcheck.domain.order.dto.request;

import com.sun.istack.NotNull;
import lombok.*;

import java.util.HashMap;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @NotNull
    private String orderId;

    @NotNull
    private String userId;

    @NotNull
    private String shippingAddress;

    @NotNull
    private HashMap<String,Integer> productQuantities;

    @NotNull
    private String paymentCode;
}
