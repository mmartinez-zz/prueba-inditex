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
