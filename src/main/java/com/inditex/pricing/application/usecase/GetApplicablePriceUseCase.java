package com.inditex.pricing.application.usecase;

import com.inditex.pricing.application.port.out.PriceRepositoryPort;
import com.inditex.pricing.domain.Price;
import com.inditex.pricing.domain.PriceQuery;
import com.inditex.pricing.domain.service.PriceSelector;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GetApplicablePriceUseCase {

    private final PriceRepositoryPort priceRepository;
    private final PriceSelector priceSelector;

    public GetApplicablePriceUseCase(PriceRepositoryPort priceRepository, PriceSelector priceSelector) {
        this.priceRepository = Objects.requireNonNull(priceRepository, "priceRepository cannot be null");
        this.priceSelector = Objects.requireNonNull(priceSelector, "priceSelector cannot be null");
    }

    public Optional<Price> getApplicablePrice(PriceQuery query) {
        Objects.requireNonNull(query, "query cannot be null");

        List<Price> prices = priceRepository.findPricesByProductAndBrand(
                query.getProductId(),
                query.getBrandId()
        );

        return priceSelector.selectApplicablePrice(prices, query.getApplicationDate());
    }
}
