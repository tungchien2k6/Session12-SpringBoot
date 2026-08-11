package com.ra.session12.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyUpdateDTO {
    private String name;
    private String specification;
    private String provider;
}