# Pricing Service

Spring Boot service that exposes a REST endpoint to retrieve the applicable price for a product based on a given date, brand and product identifier.

## Features

- Hexagonal architecture (domain, application, infrastructure)
- Priority-based price selection with deterministic behavior
- REST API with validation and centralized error handling
- In-memory H2 database initialized with sample data
- Unit and integration tests covering business scenarios

## Build

Project built with Maven:

```bash
mvn clean install
```

- Deterministic behavior is defined in case of priority tie

## Application Layer

The application logic is handled through the use case:

- GetApplicablePriceUseCase

This component orchestrates the flow by:
- Retrieving data through `PriceRepositoryPort`
- Delegating the price selection logic to `PriceSelector`

## Infrastructure

Persistence is implemented using Spring Data JPA with an in-memory H2 database.

- `PriceEntity` maps the database structure
- `PriceJpaRepository` provides data access
- `PriceRepositoryAdapter` bridges the domain with persistence
- `PriceMapper` handles entity-to-domain conversion

## API Testing

## Endpoint: GET /prices

### Parameters
- `applicationDate`: Application date and time (ISO format: YYYY-MM-DDTHH:mm:ss)
- `productId`: Product ID
- `brandId`: Brand ID

### Examples

#### Test 1: 10:00 on day 14 for product 35455 and brand 1
```bash
curl "http://localhost:8080/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"
```

**Expected response:**
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 35.50
}
```

#### Test 2: 16:00 on day 14 for product 35455 and brand 1
```bash
curl "http://localhost:8080/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"
```

**Expected response:**
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45
}
```

#### Test 3: 21:00 on day 14 for product 35455 and brand 1
```bash
curl "http://localhost:8080/prices?applicationDate=2020-06-14T21:00:00&productId=35455&brandId=1"
```

**Expected response:**
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 35.50
}
```

#### Test 4: 10:00 on day 15 for product 35455 and brand 1
```bash
curl "http://localhost:8080/prices?applicationDate=2020-06-15T10:00:00&productId=35455&brandId=1"
```

**Expected response:**
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 3,
  "startDate": "2020-06-15T00:00:00",
  "endDate": "2020-06-15T11:00:00",
  "price": 30.50
}
```

#### Test 5: 21:00 on day 16 for product 35455 and brand 1
```bash
curl "http://localhost:8080/prices?applicationDate=2020-06-16T21:00:00&productId=35455&brandId=1"
```

**Expected response:**
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 4,
  "startDate": "2020-06-15T16:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 38.95
}
```

#### Case with no applicable price (404)
```bash
curl -i "http://localhost:8080/prices?applicationDate=2025-01-01T10:00:00&productId=99999&brandId=1"
```

**Expected response:**
```json
{
    "timestamp": "2026-04-10T11:58:52.276895",
    "status": 404,
    "error": "Not Found",
    "message": "No applicable price found for productId: 99999, brandId: 1, applicationDate: 2025-01-01T10:00"
}
```

## Start the application

```bash
mvn spring-boot:run
```

Application will be available at `http://localhost:8080`
