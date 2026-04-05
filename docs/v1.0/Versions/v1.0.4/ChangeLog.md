# 📑 CHANGELOG - Habitly v1.0.4 💸

## 📅 Status: In Development / Planned
**Current Roadmap Stage:** Etapa 4 — Lógica Económica y Gestión de Suministros.

---

## 🇪🇸 Castellano (Spanish)

### 🚀 Nuevas Funcionalidades
- **Gestión de Suministros:** Incorporación de un sistema de cargos adicionales para **Luz, Agua y Comunidad**.
- **Refactorización de Deuda:** El método `getPendienteDePago()` ahora calculará: `(Precio Base + IGIC) + Gastos Extra`.
- **Historial de Gastos:** Nueva estructura interna para almacenar qué facturas se han cargado a cada vivienda.

### 🛠️ Mejoras Técnicas
- **Interfaz de Cobro:** Actualización del menú principal para permitir al Propietario elegir entre cobrar "Alquiler Mensual" o "Facturas de Suministros".
- **Balance de Beneficio:** Implementación de lógica para calcular la rentabilidad real restando gastos de mantenimiento al ingreso bruto.
- **Validación Económica:** Extensión de la batería de tests para cubrir el nuevo cálculo de deuda compuesta.

---

## 🇺🇸 English (English)

### 🚀 New Features
- **Utilities Management:** Incorporation of an additional charge system for **Electricity, Water, and HOA fees**.
- **Debt Refactoring:** The `getPendienteDePago()` method will now calculate: `(Base Price + Tax) + Extra Expenses`.
- **Expense Tracking:** New internal structure to store which invoices have been charged to each property.

### 🛠️ Technical Improvements
- **Payment Interface:** Main menu update allowing the Landlord to choose between "Monthly Rent" or "Utilities" collection.
- **Profit Balance:** Implementation of logic to calculate real profitability by subtracting maintenance expenses from gross income.
- **Economic Validation:** Extension of the unit test battery to cover the new compound debt calculation.

---

## 📊 Impacto en el Modelo de Datos / Data Model Impact

| Módulo (Module) | Cambio Técnico (Technical Change) | Propósito (Purpose) |
| :--- | :--- | :--- |
| **`Vivienda`** | `ArrayList<Gasto> listaGastos` | Almacenar facturas individuales por mes. |
| **`Gasto`** | `New Class: concepto, importe, fecha` | Clase auxiliar para tipificar los suministros. |
| **`UI / Main`** | `Submenú de Gestión Económica` | Diferenciar entre pagos de renta y abonos de suministros. |

---
*Habitly Project - DevNaranjo v1.0.4* 🏛️✅