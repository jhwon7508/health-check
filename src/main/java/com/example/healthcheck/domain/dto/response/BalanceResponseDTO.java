package com.example.healthcheck.domain.dto.response;

import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.Min;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceResponseDTO {
    private String userId;
    private Long originalBalance;
    private Long updatedBalance;

}
