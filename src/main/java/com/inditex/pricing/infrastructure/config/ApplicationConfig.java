package com.inditex.pricing.infrastructure.config;

import com.inditex.pricing.application.port.out.PriceRepositoryPort;
import com.inditex.pricing.application.usecase.GetApplicablePriceUseCase;
import com.inditex.pricing.domain.service.PriceSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public PriceSelector priceSelector() {
        return new PriceSelector();
    }

    @Bean
    public GetApplicablePriceUseCase getApplicablePriceUseCase(
            PriceRepositoryPort priceRepositoryPort,
            PriceSelector priceSelector) {
        return new GetApplicablePriceUseCase(priceRepositoryPort, priceSelector);
    }
}
