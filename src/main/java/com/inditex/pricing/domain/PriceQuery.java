package com.inditex.pricing.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public final class PriceQuery {
    private final LocalDateTime applicationDate;
    private final Long productId;
    private final Long brandId;

    private PriceQuery(Builder builder) {
        this.applicationDate = Objects.requireNonNull(builder.applicationDate, "applicationDate cannot be null");
        this.productId = Objects.requireNonNull(builder.productId, "productId cannot be null");
        this.brandId = Objects.requireNonNull(builder.brandId, "brandId cannot be null");
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private LocalDateTime applicationDate;
        private Long productId;
        private Long brandId;

        private Builder() {
        }

        public Builder applicationDate(LocalDateTime applicationDate) {
            this.applicationDate = applicationDate;
            return this;
        }

        public Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder brandId(Long brandId) {
            this.brandId = brandId;
            return this;
        }

        public PriceQuery build() {
            return new PriceQuery(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceQuery that = (PriceQuery) o;
        return Objects.equals(applicationDate, that.applicationDate) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(brandId, that.brandId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationDate, productId, brandId);
    }

    @Override
    public String toString() {
        return "PriceQuery{" +
                "applicationDate=" + applicationDate +
                ", productId=" + productId +
                ", brandId=" + brandId +
                '}';
    }
}
