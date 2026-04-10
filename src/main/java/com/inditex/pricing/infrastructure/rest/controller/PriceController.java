package com.inditex.pricing.infrastructure.rest.controller;

import com.inditex.pricing.application.usecase.GetApplicablePriceUseCase;
import com.inditex.pricing.domain.Price;
import com.inditex.pricing.domain.PriceQuery;
import com.inditex.pricing.infrastructure.rest.dto.PriceResponse;
import com.inditex.pricing.infrastructure.rest.mapper.PriceResponseMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/prices")
public class PriceController {

    private final GetApplicablePriceUseCase getApplicablePriceUseCase;

    public PriceController(GetApplicablePriceUseCase getApplicablePriceUseCase) {
        this.getApplicablePriceUseCase = Objects.requireNonNull(getApplicablePriceUseCase, 
                "getApplicablePriceUseCase cannot be null");
    }

    @GetMapping
    public ResponseEntity<PriceResponse> getApplicablePrice(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam Long productId,
            @RequestParam Long brandId) {

        PriceQuery query = PriceQuery.builder()
                .applicationDate(applicationDate)
                .productId(productId)
                .brandId(brandId)
                .build();

        Optional<Price> price = getApplicablePriceUseCase.getApplicablePrice(query);

        return price
                .map(PriceResponseMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
