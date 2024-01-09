package com.example.healthcheck.domain.product.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PopularProductDTO {
    private Long productIdx;
    private String productName;
    private Long price;
}
