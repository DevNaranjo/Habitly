# ChangeLog - Habitly 🏛️

## [1.0.21] - 2026-04-05
### 🏁 Etapa 2: Modelo de Vivienda (COMPLETADA)

### Añadido 🚀
- **Atributo Amueblado:** Incorporado el campo `boolean estaAmueblado` en toda la jerarquía de viviendas.
- **Sistema de Siglas [GPA]:** Nueva visualización en el inventario para identificar rápidamente:
  - **G**: Garaje
  - **P**: Piscina
  - **A**: Amueblado
- **Ratio de Rentabilidad:** Visualización del precio por metro cuadrado (`EUR/m2`) en la tabla principal.

### Cambios 🛠️
- **Refactorización de Constructores:** Actualización de `super()` en las clases `Piso` y `Casa` para soportar la nueva estructura de 9 parámetros de la clase madre.
- **Flujo de Usuario:** Se ha modificado el ciclo principal del programa. Ahora, realizar un "Borrado de Fábrica" (Opción 7) cierra la aplicación automáticamente para evitar inconsistencias en memoria.

### Corregido 🩹
- **Inconsistencia de Identificadores:** Corregido error en el constructor de `Piso` que invocaba erróneamente a la clase `Casa`.
- **Persistencia:** Sincronización del sistema de guardado con los nuevos atributos booleanos.

---
*Desarrollado por DevNaranjo - "Construyendo el futuro de la gestión inmobiliaria."*