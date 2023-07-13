package com.ecommerce.visibilidad.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SizeCsvFileDTO {
    private Integer sizeId;
    private Integer productId;
    private boolean backSoon;
    private boolean special;
}
