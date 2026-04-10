package com.inditex.pricing.infrastructure.adapter;

import com.inditex.pricing.application.port.out.PriceRepositoryPort;
import com.inditex.pricing.domain.Price;
import com.inditex.pricing.infrastructure.persistence.entity.PriceEntity;
import com.inditex.pricing.infrastructure.persistence.mapper.PriceMapper;
import com.inditex.pricing.infrastructure.persistence.repository.PriceJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final PriceJpaRepository priceJpaRepository;

    public PriceRepositoryAdapter(PriceJpaRepository priceJpaRepository) {
        this.priceJpaRepository = Objects.requireNonNull(priceJpaRepository, "priceJpaRepository cannot be null");
    }

    @Override
    public List<Price> findPricesByProductAndBrand(Long productId, Long brandId) {
        List<PriceEntity> entities = priceJpaRepository.findByProductIdAndBrandId(productId, brandId);
        
        return entities.stream()
                .map(PriceMapper::toDomain)
                .toList();
    }
}
