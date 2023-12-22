package com.example.healthcheck.domain.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private String productCode;
    private String productName;
    private Long price;
    private Integer stockQuantity;
    private Boolean isSoldOut;
}
