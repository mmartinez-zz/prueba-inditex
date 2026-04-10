package com.inditex.pricing.infrastructure.rest.controller;

import com.inditex.pricing.application.usecase.GetApplicablePriceUseCase;
import com.inditex.pricing.domain.Price;
import com.inditex.pricing.domain.PriceQuery;
import com.inditex.pricing.infrastructure.rest.dto.PriceResponse;
import com.inditex.pricing.infrastructure.rest.mapper.PriceResponseMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/prices")
public class PriceController {

    private final GetApplicablePriceUseCase getApplicablePriceUseCase;

    public PriceController(GetApplicablePriceUseCase getApplicablePriceUseCase) {
        this.getApplicablePriceUseCase = Objects.requireNonNull(getApplicablePriceUseCase,
                "getApplicablePriceUseCase cannot be null");
    }

    @GetMapping
    public PriceResponse getApplicablePrice(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam Long productId,
            @RequestParam Long brandId) {

        validateParameters(applicationDate, productId, brandId);

        PriceQuery query = PriceQuery.builder()
                .applicationDate(applicationDate)
                .productId(productId)
                .brandId(brandId)
                .build();

        Price price = getApplicablePriceUseCase.getApplicablePrice(query)
                .orElseThrow(() -> new NoSuchElementException(
                        "No applicable price found for productId: " + productId +
                        ", brandId: " + brandId +
                        ", applicationDate: " + applicationDate));

        return PriceResponseMapper.toResponse(price);
    }

    private void validateParameters(LocalDateTime applicationDate, Long productId, Long brandId) {
        if (applicationDate == null) {
            throw new IllegalArgumentException("applicationDate cannot be null");
        }
        if (productId == null) {
            throw new IllegalArgumentException("productId cannot be null");
        }
        if (brandId == null) {
            throw new IllegalArgumentException("brandId cannot be null");
        }
    }
}
