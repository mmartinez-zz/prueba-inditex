package com.inditex.pricing.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public final class Price {
    private final Long priceList;
    private final Long brandId;
    private final Long productId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Integer priority;
    private final BigDecimal price;
    private final String currency;

    private Price(Builder builder) {
        this.priceList = Objects.requireNonNull(builder.priceList, "priceList cannot be null");
        this.brandId = Objects.requireNonNull(builder.brandId, "brandId cannot be null");
        this.productId = Objects.requireNonNull(builder.productId, "productId cannot be null");
        this.startDate = Objects.requireNonNull(builder.startDate, "startDate cannot be null");
        this.endDate = Objects.requireNonNull(builder.endDate, "endDate cannot be null");
        this.priority = Objects.requireNonNull(builder.priority, "priority cannot be null");
        this.price = Objects.requireNonNull(builder.price, "price cannot be null");
        this.currency = Objects.requireNonNull(builder.currency, "currency cannot be null");
    }

    public Long getPriceList() {
        return priceList;
    }

    public Long getBrandId() {
        return brandId;
    }

    public Long getProductId() {
        return productId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long priceList;
        private Long brandId;
        private Long productId;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Integer priority;
        private BigDecimal price;
        private String currency;

        private Builder() {
        }

        public Builder priceList(Long priceList) {
            this.priceList = priceList;
            return this;
        }

        public Builder brandId(Long brandId) {
            this.brandId = brandId;
            return this;
        }

        public Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder startDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDateTime endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder priority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Price build() {
            return new Price(this);
        }
    }

    public boolean isApplicable(LocalDateTime date) {
        return (date.isEqual(startDate) || date.isAfter(startDate)) &&
               (date.isEqual(endDate) || date.isBefore(endDate));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return Objects.equals(priceList, price1.priceList) &&
                Objects.equals(brandId, price1.brandId) &&
                Objects.equals(productId, price1.productId) &&
                Objects.equals(startDate, price1.startDate) &&
                Objects.equals(endDate, price1.endDate) &&
                Objects.equals(priority, price1.priority) &&
                Objects.equals(price, price1.price) &&
                Objects.equals(currency, price1.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(priceList, brandId, productId, startDate, endDate, priority, price, currency);
    }

    @Override
    public String toString() {
        return "Price{" +
                "priceList=" + priceList +
                ", brandId=" + brandId +
                ", productId=" + productId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", priority=" + priority +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                '}';
    }
}
