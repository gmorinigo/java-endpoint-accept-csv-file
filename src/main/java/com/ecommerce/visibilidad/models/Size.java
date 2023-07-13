package com.ecommerce.visibilidad.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size {
    private Integer sizeId;
    private Integer productId;
    private boolean backSoon;
    private boolean special;
    private Integer quantity;
}
