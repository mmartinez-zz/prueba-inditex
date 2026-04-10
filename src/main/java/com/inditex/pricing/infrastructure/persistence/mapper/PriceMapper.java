package com.inditex.pricing.infrastructure.persistence.mapper;

import com.inditex.pricing.domain.Price;
import com.inditex.pricing.infrastructure.persistence.entity.PriceEntity;

public class PriceMapper {

    private PriceMapper() {
    }

    public static Price toDomain(PriceEntity entity) {
        if (entity == null) {
            return null;
        }

        return Price.builder()
                .priceList(entity.getPriceList())
                .brandId(entity.getBrandId())
                .productId(entity.getProductId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .priority(entity.getPriority())
                .price(entity.getPrice())
                .currency(entity.getCurrency())
                .build();
    }
}
