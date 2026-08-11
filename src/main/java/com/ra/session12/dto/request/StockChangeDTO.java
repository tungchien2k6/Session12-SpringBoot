package com.ra.session12.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockChangeDTO {
    @NotNull(message = "amount không được để trống")
    @Min(value = 1, message = "amount phải lớn hơn 0")
    private Integer amount;
}