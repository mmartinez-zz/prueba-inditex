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

    @Test
    @DisplayName("Cuando no hay precios aplicables, debe retornar Optional vacío")
    void shouldReturnEmptyOptionalWhenNoPricesAreApplicable() {
        LocalDateTime applicationDate = LocalDateTime.of(2024, 6, 14, 16, 0);

        Price priceBeforeRange = Price.builder()
                .priceList(1L)
                .brandId(1L)
                .productId(35455L)
                .startDate(LocalDateTime.of(2024, 1, 1, 0, 0))
                .endDate(LocalDateTime.of(2024, 3, 31, 23, 59, 59))
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        Price priceAfterRange = Price.builder()
                .priceList(2L)
                .brandId(1L)
                .productId(35455L)
                .startDate(LocalDateTime.of(2024, 12, 1, 0, 0))
                .endDate(LocalDateTime.of(2024, 12, 31, 23, 59, 59))
                .priority(0)
                .price(new BigDecimal("40.00"))
                .currency("EUR")
                .build();

        List<Price> prices = List.of(priceBeforeRange, priceAfterRange);

        Optional<Price> result = priceSelector.selectApplicablePrice(prices, applicationDate);

        assertFalse(result.isPresent(), "Debe retornar Optional vacío cuando no hay precios aplicables");
    }

    @Test
    @DisplayName("Cuando solo hay un precio válido, debe retornarlo")
    void shouldReturnSingleValidPrice() {
        LocalDateTime applicationDate = LocalDateTime.of(2024, 6, 14, 16, 0);

        Price validPrice = Price.builder()
                .priceList(1L)
                .brandId(1L)
                .productId(35455L)
                .startDate(LocalDateTime.of(2024, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2024, 12, 31, 23, 59, 59))
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        List<Price> prices = List.of(validPrice);

        Optional<Price> result = priceSelector.selectApplicablePrice(prices, applicationDate);

        assertTrue(result.isPresent(), "Debe retornar un precio");
        assertEquals(validPrice, result.get(), "Debe retornar el único precio válido");
    }

    @Test
    @DisplayName("Cuando la fecha es exactamente igual a startDate, debe aplicar el precio")
    void shouldApplyPriceWhenDateEqualsStartDate() {
        LocalDateTime startDate = LocalDateTime.of(2024, 6, 14, 0, 0);

        Price price = Price.builder()
                .priceList(1L)
                .brandId(1L)
                .productId(35455L)
                .startDate(startDate)
                .endDate(LocalDateTime.of(2024, 12, 31, 23, 59, 59))
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        List<Price> prices = List.of(price);

        Optional<Price> result = priceSelector.selectApplicablePrice(prices, startDate);

        assertTrue(result.isPresent(), "Debe retornar un precio cuando la fecha es exactamente startDate");
        assertEquals(price, result.get(), "Debe retornar el precio aplicable");
    }

    @Test
    @DisplayName("Cuando la fecha es exactamente igual a endDate, debe aplicar el precio")
    void shouldApplyPriceWhenDateEqualsEndDate() {
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59, 59);

        Price price = Price.builder()
                .priceList(1L)
                .brandId(1L)
                .productId(35455L)
                .startDate(LocalDateTime.of(2024, 6, 14, 0, 0))
                .endDate(endDate)
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        List<Price> prices = List.of(price);

        Optional<Price> result = priceSelector.selectApplicablePrice(prices, endDate);

        assertTrue(result.isPresent(), "Debe retornar un precio cuando la fecha es exactamente endDate");
        assertEquals(price, result.get(), "Debe retornar el precio aplicable");
    }

    @Test
    @DisplayName("Cuando hay múltiples precios aplicables con la misma prioridad, debe retornar uno de forma determinística")
    void shouldReturnDeterministicResultWhenMultiplePricesHaveSamePriority() {
        LocalDateTime applicationDate = LocalDateTime.of(2024, 6, 14, 16, 0);

        Price firstPrice = Price.builder()
                .priceList(1L)
                .brandId(1L)
                .productId(35455L)
                .startDate(LocalDateTime.of(2024, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2024, 12, 31, 23, 59, 59))
                .priority(1)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        Price secondPrice = Price.builder()
                .priceList(2L)
                .brandId(1L)
                .productId(35455L)
                .startDate(LocalDateTime.of(2024, 6, 14, 10, 0))
                .endDate(LocalDateTime.of(2024, 6, 14, 20, 0))
                .priority(1)
                .price(new BigDecimal("40.00"))
                .currency("EUR")
                .build();

        List<Price> prices = List.of(firstPrice, secondPrice);

        Optional<Price> result = priceSelector.selectApplicablePrice(prices, applicationDate);

        assertTrue(result.isPresent(), "Debe retornar un precio");
        Price selectedPrice = result.get();
        assertEquals(secondPrice, selectedPrice);
    }
}
