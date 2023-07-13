package com.ecommerce.visibilidad.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class Product {
    private Integer productId;
    private Integer sequence;
    private List<Size> sizes;

    public boolean hasSpecialSize() {
        return sizes.stream().filter(size -> size.isSpecial()).count() > 0;
    }

    public boolean hasSpecialSizeWithStock() {
        return sizes.stream().filter(size -> size.isSpecial() && size.getQuantity() > 0).count() > 0 ;
    }

    public boolean hasNonSpecialSizeWithStock() {
        return sizes.stream().filter(size -> !size.isSpecial() && size.getQuantity() > 0).count() > 0 ;
    }

    public boolean hasSizeBackSoon() {
        return sizes.stream().filter(size -> size.isBackSoon()).count() > 0;
    }

    public boolean doesntHasSpecialSize() {
        return sizes.stream().filter(size -> size.isSpecial()).count() == 0;
    }
}
