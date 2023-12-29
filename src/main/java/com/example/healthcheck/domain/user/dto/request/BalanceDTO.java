package com.example.healthcheck.domain.user.dto.request;

import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.Min;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDTO {

    @NotNull
    private String userId;

    @Min(0)
    private Long balance;
}
