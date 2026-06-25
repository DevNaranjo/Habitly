# 🏠 Habitly - Sistema de Gestión Inmobiliaria (v1.1.5 GUI)

**Habitly** es una aplicación profesional de escritorio diseñada para la gestión integral de activos inmobiliarios. Con la versión **v1.1.5 (Full GUI)**, el sistema integra un motor de **Cumplimiento Legal (Compliance)** ajustado a la Ley de Vivienda de España (Ley 12/2023) y la LAU, incorporando cálculos fiscales, dashboards de rentabilidad y un asistente de instalación autoejecutable para Windows.

---

## 🚀 Características Principales (v1.1.5)

* 🖥️ **Interfaz Gráfica Premium (Dark Mode):** Una UI interactiva construida sobre Java Swing con una paleta de colores oscura (Slate Theme) muy moderna. Cuenta con efectos de foco realzados, barras de navegación laterales, diseño responsivo y tablas con anchos mínimos configurados.
* 📦 **Asistente de Instalación Windows (`wizardSetUp.exe`):** Distribución simplificada mediante un instalador tipo Wizard (“Siguiente, Siguiente, Instalar”). Empaqueta su propio JRE (Java Runtime Environment) reducido, por lo que no es necesario tener Java instalado en el ordenador para ejecutar Habitly.
* ⚖️ **Cumplimiento Legal (Ley 12/2023 y LAU):** Motor de validación de contratos que bloquea automáticamente cualquier alquiler que supere el límite legal de renta mensual establecido por el índice oficial IRAV para zonas tensionadas.
* 📜 **Ciclo de Vida del Contrato:** La entidad `ContratoAlquiler` fuerza automáticamente la normativa:
    * **Duración Mínima:** 5 años para personas físicas y 7 años para entidades jurídicas (Art. 9 LAU).
    * **Desistimiento:** Verifica el mínimo obligatorio de 6 meses antes de permitir salidas unilaterales.
    * **Prórroga Tácita:** Extensión automática anual del contrato hasta un límite legal de 3 años (Art. 10 LAU).
    * **Actualización Anual de Renta:** Reajuste automático en la fecha de aniversario utilizando el índice oficial.
* 🇮🇨 **Fiscalidad de Canarias (IGIC):** Núcleo financiero adaptado, que aplica automáticamente la exención del IGIC (0%) en viviendas de alquiler habitual, manteniendo el soporte para alquileres turísticos.
* 🏦 **Fianzas y Garantías (ICAVI):** Validación estricta del depósito legal (exactamente 1 mes) y garantías adicionales (máximo 2 meses), con seguimiento de los depósitos oficiales en el ICAVI.
* 📊 **Dashboard de Rentabilidad:** Visualización interactiva en tiempo real del margen neto (ingresos de alquiler totales menos gastos registrados) de manera consolidada y por unidad.
* 🔐 **Cifrado de Contraseñas y Seguridad (AES-GCM):** Almacenamiento súper seguro mediante AES-256 en modo GCM, con derivación de clave por PBKDF2 y salting dinámico. Las contraseñas en memoria se manejan estrictamente como arrays `char[]` para evitar fugas y volcados de memoria (Garbage Collector).

---

## 🛠️ Instalación y Uso

La versión **v1.1.5** está pensada para ser instalada directamente en Windows sin requerir configuraciones de entorno de desarrollo:

1. **Descarga el instalador:** Descarga el archivo `wizardSetUp.exe` disponible en este repositorio.
2. **Ejecuta el asistente:** Haz doble clic sobre `wizardSetUp.exe` y sigue el asistente de instalación interactivo.
3. **Inicia la aplicación:** El instalador creará un acceso directo en tu Escritorio y en tu menú de Inicio con el logotipo oficial de Habitly.

---

Developed with ❤️ by DevNaranjo
