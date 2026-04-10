package com.inditex.pricing.infrastructure.adapter;

import com.inditex.pricing.domain.Price;
import com.inditex.pricing.infrastructure.persistence.entity.PriceEntity;
import com.inditex.pricing.infrastructure.persistence.repository.PriceJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(PriceRepositoryAdapter.class)
class PriceRepositoryAdapterIntegrationTest {

    @Autowired
    private PriceRepositoryAdapter adapter;

    @Autowired
    private PriceJpaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Debe retornar precios del producto y marca especificados")
    void shouldReturnPricesByProductAndBrand() {
        PriceEntity entity1 = createPriceEntity(1L, 1L, 35455L, 0, new BigDecimal("35.50"));
        PriceEntity entity2 = createPriceEntity(2L, 1L, 35455L, 1, new BigDecimal("25.45"));
        PriceEntity entity3 = createPriceEntity(3L, 1L, 99999L, 0, new BigDecimal("40.00"));

        repository.save(entity1);
        repository.save(entity2);
        repository.save(entity3);

        List<Price> prices = adapter.findPricesByProductAndBrand(35455L, 1L);

        assertEquals(2, prices.size());
        assertTrue(prices.stream().allMatch(p -> p.getProductId().equals(35455L)));
        assertTrue(prices.stream().allMatch(p -> p.getBrandId().equals(1L)));
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay precios para el producto y marca")
    void shouldReturnEmptyListWhenNoPricesFound() {
        List<Price> prices = adapter.findPricesByProductAndBrand(99999L, 1L);

        assertTrue(prices.isEmpty());
    }

    @Test
    @DisplayName("Debe mapear correctamente PriceEntity a Price")
    void shouldMapEntityToDomainCorrectly() {
        PriceEntity entity = createPriceEntity(1L, 1L, 35455L, 0, new BigDecimal("35.50"));
        repository.save(entity);

        List<Price> prices = adapter.findPricesByProductAndBrand(35455L, 1L);

        assertEquals(1, prices.size());
        Price price = prices.get(0);
        assertEquals(entity.getPriceList(), price.getPriceList());
        assertEquals(entity.getBrandId(), price.getBrandId());
        assertEquals(entity.getProductId(), price.getProductId());
        assertEquals(entity.getStartDate(), price.getStartDate());
        assertEquals(entity.getEndDate(), price.getEndDate());
        assertEquals(entity.getPriority(), price.getPriority());
        assertEquals(entity.getPrice(), price.getPrice());
        assertEquals(entity.getCurrency(), price.getCurrency());
    }

    private PriceEntity createPriceEntity(Long priceList, Long brandId, Long productId, 
                                          Integer priority, BigDecimal price) {
        PriceEntity entity = new PriceEntity();
        entity.setPriceList(priceList);
        entity.setBrandId(brandId);
        entity.setProductId(productId);
        entity.setStartDate(LocalDateTime.of(2020, 6, 14, 0, 0));
        entity.setEndDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
        entity.setPriority(priority);
        entity.setPrice(price);
        entity.setCurrency("EUR");
        return entity;
    }
}
