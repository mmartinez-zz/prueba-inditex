package com.inditex.pricing.application.port.out;

import com.inditex.pricing.domain.Price;

import java.util.List;

public interface PriceRepositoryPort {

    List<Price> findPricesByProductAndBrand
    (Long productId, Long brandId);
}
