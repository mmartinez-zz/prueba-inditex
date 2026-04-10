package com.inditex.pricing.domain.service;

import com.inditex.pricing.domain.Price;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PriceSelector {

    public Optional<Price> selectApplicablePrice(List<Price> prices, LocalDateTime applicationDate) {
        return prices.stream()
                .filter(price -> price.isApplicable(applicationDate))
                .max(
                    Comparator.comparing(Price::getPriority)
                              .thenComparing(Price::getStartDate)
                );
    }
}
