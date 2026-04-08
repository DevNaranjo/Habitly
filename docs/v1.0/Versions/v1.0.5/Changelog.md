# Changelog - Habitly

## [1.0.5] - 2026-04-08
### Añadido
- **Módulo de Compliance**: Integración de la Ley 12/2023 por el Derecho a la Vivienda.
- **Clase `ContratoAlquiler`**: Soporte para series de contratos, gestión de fianzas y tipos de arrendador (Físico/Jurídico).
- **Control de Fianzas**: Lógica para la detección de depósitos pendientes ante el ICAVI.
- **Auditoría Interna**: Clase `ValidadorLegalCompliance` para tests de estrés normativo.

### Cambiado
- **`Vivienda`**: Ahora soporta el atributo `limiteMaximoIrav` y vinculación dinámica de `ContratoActivo`.
- **`GestorInventario`**: El método `formalizarContrato` ahora actúa como un "gatekeeper" legal, rechazando operaciones fuera de índice.
- **Persistencia**: Se ha actualizado la `CajaFuerte` para incluir los nuevos estados de ocupación legal.

### Seguridad
- Refuerzo en la integridad de datos: los contratos ahora se validan antes de ser persistidos mediante AES.