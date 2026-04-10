package com.inditex.pricing.service;

import com.inditex.pricing.domain.Price;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PriceSelector {

    public Optional<Price> selectApplicablePrice(List<Price> prices, LocalDateTime applicationDate) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
