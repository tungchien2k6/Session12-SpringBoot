package com.ra.session12.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplyCreateDTO {
    @NotBlank(message = "Tên vật tư không được để trống")
    private String name;

    private String provider;
    private String specification;
    private String unit;
}