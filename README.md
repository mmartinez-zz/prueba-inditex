Pricing Domain

Se ha comenzado la implementación del dominio para la resolución de precios.

Actualmente se modela:

- Entidad `Price`
- Objeto de consulta `PriceQuery`
- Servicio de dominio para selección de precios basado en:
  - Rango de fechas
  - Prioridad

Se ha implementado la lógica inicial mediante TDD.

## Build

Proyecto construido con Maven:

```bash
mvn clean install
```

- Se define comportamiento determinístico en caso de empate de prioridad

## Application Layer

Se introduce la capa de aplicación mediante el caso de uso:

- GetApplicablePriceUseCase

Este orquesta la lógica de negocio delegando en:
- PriceRepositoryPort (acceso a datos)
- PriceSelector (lógica de dominio)

## Infrastructure

Se implementa el adapter de persistencia utilizando Spring Data JPA y H2.

- PriceEntity (entidad JPA)
- PriceJpaRepository
- PriceRepositoryAdapter
- PriceMapper (entity → domain)

## API Testing

## Endpoint: GET /prices

### Parámetros
- `applicationDate`: Fecha y hora de aplicación (formato ISO: YYYY-MM-DDTHH:mm:ss)
- `productId`: ID del producto
- `brandId`: ID de la marca

### Ejemplos de uso

#### Test 1: 10:00 del día 14 para producto 35455 y marca 1
```bash
curl "http://localhost:8080/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"
```

**Respuesta esperada:**
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

#### Test 2: 16:00 del día 14 para producto 35455 y marca 1
```bash
curl "http://localhost:8080/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"
```

**Respuesta esperada:**
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

#### Test 3: 21:00 del día 14 para producto 35455 y marca 1
```bash
curl "http://localhost:8080/prices?applicationDate=2020-06-14T21:00:00&productId=35455&brandId=1"
```

**Respuesta esperada:**
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

#### Test 4: 10:00 del día 15 para producto 35455 y marca 1
```bash
curl "http://localhost:8080/prices?applicationDate=2020-06-15T10:00:00&productId=35455&brandId=1"
```

**Respuesta esperada:**
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

#### Test 5: 21:00 del día 16 para producto 35455 y marca 1
```bash
curl "http://localhost:8080/prices?applicationDate=2020-06-16T21:00:00&productId=35455&brandId=1"
```

**Respuesta esperada:**
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

#### Caso sin precio aplicable (404)
```bash
curl -i "http://localhost:8080/prices?applicationDate=2025-01-01T10:00:00&productId=99999&brandId=1"
```

**Respuesta esperada:**
```
HTTP/1.1 404 Not Found
```

## Iniciar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`
