package com.inditex.pricing.application.usecase;

import com.inditex.pricing.application.port.out.PriceRepositoryPort;
import com.inditex.pricing.domain.Price;
import com.inditex.pricing.domain.PriceQuery;
import com.inditex.pricing.domain.service.PriceSelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GetApplicablePriceUseCaseTest {

    private GetApplicablePriceUseCase useCase;
    private TestPriceRepository priceRepository;
    private PriceSelector priceSelector;

    @BeforeEach
    void setUp() {
        priceRepository = new TestPriceRepository();
        priceSelector = new PriceSelector();
        useCase = new GetApplicablePriceUseCase(priceRepository, priceSelector);
    }

    @Test
    @DisplayName("Debe retornar el precio aplicable cuando existe en el repositorio")
    void shouldReturnApplicablePriceWhenExists() {
        LocalDateTime applicationDate = LocalDateTime.of(2024, 6, 14, 16, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        Price price = Price.builder()
                .priceList(1L)
                .brandId(brandId)
                .productId(productId)
                .startDate(LocalDateTime.of(2024, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2024, 12, 31, 23, 59, 59))
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        priceRepository.addPrice(price);

        PriceQuery query = PriceQuery.builder()
                .applicationDate(applicationDate)
                .productId(productId)
                .brandId(brandId)
                .build();

        Optional<Price> result = useCase.getApplicablePrice(query);

        assertTrue(result.isPresent());
        assertEquals(price, result.get());
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando no hay precios en el repositorio")
    void shouldReturnEmptyWhenNoPricesInRepository() {
        PriceQuery query = PriceQuery.builder()
                .applicationDate(LocalDateTime.of(2024, 6, 14, 16, 0))
                .productId(35455L)
                .brandId(1L)
                .build();

        Optional<Price> result = useCase.getApplicablePrice(query);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Debe seleccionar el precio con mayor prioridad cuando hay múltiples precios aplicables")
    void shouldSelectHighestPriorityWhenMultiplePricesApplicable() {
        LocalDateTime applicationDate = LocalDateTime.of(2024, 6, 14, 16, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        Price lowPriorityPrice = Price.builder()
                .priceList(1L)
                .brandId(brandId)
                .productId(productId)
                .startDate(LocalDateTime.of(2024, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2024, 12, 31, 23, 59, 59))
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        Price highPriorityPrice = Price.builder()
                .priceList(2L)
                .brandId(brandId)
                .productId(productId)
                .startDate(LocalDateTime.of(2024, 6, 14, 15, 0))
                .endDate(LocalDateTime.of(2024, 6, 14, 18, 30))
                .priority(1)
                .price(new BigDecimal("25.45"))
                .currency("EUR")
                .build();

        priceRepository.addPrice(lowPriorityPrice);
        priceRepository.addPrice(highPriorityPrice);

        PriceQuery query = PriceQuery.builder()
                .applicationDate(applicationDate)
                .productId(productId)
                .brandId(brandId)
                .build();

        Optional<Price> result = useCase.getApplicablePrice(query);

        assertTrue(result.isPresent());
        assertEquals(highPriorityPrice, result.get());
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando los precios del repositorio no son aplicables")
    void shouldReturnEmptyWhenPricesNotApplicable() {
        LocalDateTime applicationDate = LocalDateTime.of(2024, 6, 14, 16, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        Price priceOutOfRange = Price.builder()
                .priceList(1L)
                .brandId(brandId)
                .productId(productId)
                .startDate(LocalDateTime.of(2024, 1, 1, 0, 0))
                .endDate(LocalDateTime.of(2024, 3, 31, 23, 59, 59))
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        priceRepository.addPrice(priceOutOfRange);

        PriceQuery query = PriceQuery.builder()
                .applicationDate(applicationDate)
                .productId(productId)
                .brandId(brandId)
                .build();

        Optional<Price> result = useCase.getApplicablePrice(query);

        assertFalse(result.isPresent());
    }

    private static class TestPriceRepository implements PriceRepositoryPort {
        private List<Price> prices = Collections.emptyList();

        public void addPrice(Price price) {
            prices = new java.util.ArrayList<>(prices);
            prices.add(price);
        }

        @Override
        public List<Price> findPricesByProductAndBrand(Long productId, Long brandId) {
            return prices.stream()
                    .filter(p -> p.getProductId().equals(productId) && p.getBrandId().equals(brandId))
                    .toList();
        }
    }
}
