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