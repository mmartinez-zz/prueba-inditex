package com.inditex.pricing.infrastructure.rest.mapper;

import com.inditex.pricing.domain.Price;
import com.inditex.pricing.infrastructure.rest.dto.PriceResponse;

public class PriceResponseMapper {

    private PriceResponseMapper() {
    }

    public static PriceResponse toResponse(Price price) {
        if (price == null) {
            return null;
        }

        return new PriceResponse(
                price.getProductId(),
                price.getBrandId(),
                price.getPriceList(),
                price.getStartDate(),
                price.getEndDate(),
                price.getPrice()
        );
    }
}
