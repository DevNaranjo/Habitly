# Changelog - Habitly

## [1.0.6] - 2026-05-22

### Añadido
- **Módulo Legal y Compliance Avanzado (Fase 3 - Ley 12/2023 & LAU 2026)**:
  - **Enum `EstadoContrato`**: Nuevos estados de contratos (`VIGENTE`, `EN_PRORROGA_TACITA`, `FINALIZADO`, `DESISTIDO`, `RESUELTO`).
  - **Prórroga Obligatoria**: Validación automática de duraciones mínimas legales (5 años/60 meses para personas físicas y 7 años/84 meses para personas jurídicas) según el Art. 9 de la LAU.
  - **Prórroga Tácita**: Lógica y control para la prórroga automática anual de hasta 3 años de duración máxima (Art. 10 de la LAU).
  - **Fianza y Garantías Legales**: Validación estricta del depósito de fianza (exactamente 1 mensualidad) y del límite legal de garantías adicionales (máximo de 2 mensualidades) (Art. 36 de la LAU).
  - **Control de Desistimiento**: Regulación de desistimiento del inquilino solo tras transcurrir un mínimo de 6 meses desde la fecha de firma.
  - **Actualización Anual por IRAV**: Algoritmo para reajustar los precios de alquiler basándose en el porcentaje oficial del Índice de Referencia de Arrendamiento de Vivienda (IRAV) en la fecha del aniversario del contrato.
  - **Depósito Autonómico (ICAVI)**: Seguimiento de depósitos ante el Instituto Canario de la Vivienda y plazos límite de 30 días.
  - **Dashboard de Cumplimiento Legal**: Nueva sección interactiva en la UI que consolida alertas de vencimiento, detección de fianzas pendientes e indicadores globales de compliance.

- **Nuevas Funcionalidades del Propietario (Fase 2)**:
  - **Dashboard de Beneficios**: Cálculo y visualización premium del beneficio neto (ingresos de rentas cobradas menos el historial de gastos registrados de las viviendas).
  - **Eliminación Física de Viviendas**: Posibilidad de borrar propiedades individuales del inventario general de forma segura, con salvaguardas que impiden eliminar propiedades no disponibles o actualmente arrendadas.

- **Robustez y UX (Mejoras en el Main)**:
  - Bucle de sesión persistente en la entrada de la aplicación para permitir múltiples inicios de sesión sin terminar el proceso de ejecución abruptamente.
  - Aislamiento volátil del **Modo Invitado (Guest)** para evitar la persistencia en disco de datos temporales.
  - Parser blindado de entrada por teclado (`leerDouble` y `leerEntero`) con tolerancia de localización decimal (comas/puntos) y rechazo de valores vacíos o no positivos.

### Cambiado
- **Exención Fiscal**: Modificados los cálculos de impuestos de `Piso` y `Casa` para excluir automáticamente el 7% de IGIC sobre alquileres residenciales habituales, alineando la contabilidad con el régimen fiscal de Canarias.
- **Unificación de Consultas**: Eliminado el método redundante `buscarPorDni(String)` en `GestorInventario` en favor de `obtenerUsuario(String)`.
- **Actualización de Versiones**: Unificadas todas las cabeceras `@version` de las clases del proyecto al valor `1.0.6` para una correcta trazabilidad.

### Seguridad
- Refuerzo en la fase de formalización del contrato mediante validaciones previas automáticas antes del cifrado y guardado simétrico AES de `sistema.dat`.
