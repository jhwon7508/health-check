package com.example.healthcheck.domain.user.dto.response;

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
