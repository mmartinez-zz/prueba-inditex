package com.inditex.pricing.service;

import com.inditex.pricing.domain.Price;
import com.inditex.pricing.domain.service.PriceSelector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PriceSelectorTest {

    private PriceSelector priceSelector;

    @BeforeEach
    void setUp() {
        priceSelector = new PriceSelector();
    }

    @Test
    @DisplayName("Cuando hay múltiples precios activos en la misma fecha, debe seleccionar el de mayor prioridad")
    void shouldSelectPriceWithHighestPriorityWhenMultiplePricesAreActive() {
        LocalDateTime applicationDate = LocalDateTime.of(2024, 6, 14, 16, 0);

        Price lowPriorityPrice = Price.builder()
                .priceList(1L)
                .brandId(1L)
                .productId(35455L)
                .startDate(LocalDateTime.of(2024, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2024, 12, 31, 23, 59, 59))
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        Price highPriorityPrice = Price.builder()
                .priceList(2L)
                .brandId(1L)
                .productId(35455L)
                .startDate(LocalDateTime.of(2024, 6, 14, 15, 0))
                .endDate(LocalDateTime.of(2024, 6, 14, 18, 30))
                .priority(1)
                .price(new BigDecimal("25.45"))
                .currency("EUR")
                .build();

        List<Price> prices = List.of(lowPriorityPrice, highPriorityPrice);

        Optional<Price> result = priceSelector.selectApplicablePrice(prices, applicationDate);

        assertTrue(result.isPresent(), "Debe retornar un precio");
        assertEquals(highPriorityPrice, result.get(), "Debe seleccionar el precio con mayor prioridad");
    }
}
