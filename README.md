# 🏠 Habitly - Sistema de Gestión Inmobiliaria (v1.1.0-F GUI)

**Habitly** es una aplicación profesional de escritorio diseñada para la gestión integral de activos inmobiliarios. Con la versión **v1.1.0-F (Full GUI)**, el sistema evoluciona de la consola a una **interfaz gráfica premium interactiva (Dark Mode)**, integrando un motor de **Cumplimiento Legal (Compliance)** ajustado a la Ley de Vivienda de España (Ley 12/2023) y la LAU, incorporando cálculos fiscales, dashboards de rentabilidad y un asistente de instalación autoejecutable para Windows.

---

## 🚀 Características Principales (v1.1.0-F)

* **🖥️ Interfaz Gráfica Premium (Dark Mode):** Una UI interactiva construida sobre Java Swing con una paleta de colores oscura (Slate Theme) muy moderna. Cuenta con efectos de foco realzados, barras de navegación laterales, diseño responsivo y tablas con anchos mínimos configurados.
* **📦 Asistente de Instalación Windows (`wizardSetUp.exe`):** Distribución simplificada mediante un instalador tipo Wizard ("Siguiente, Siguiente, Instalar"). Empaqueta su propio JRE (Java Runtime Environment) reducido, por lo que **no es necesario tener Java instalado en el ordenador** para ejecutar Habitly.
* **⚖️ Cumplimiento Legal (Ley 12/2023 y LAU):** Motor de validación de contratos que bloquea automáticamente cualquier alquiler que supere el límite legal de renta mensual establecido por el índice oficial **IRAV** para zonas tensionadas.
* **📜 Ciclo de Vida del Contrato:** La entidad `ContratoAlquiler` fuerza automáticamente la normativa:
    * **Duración Mínima:** 5 años para personas físicas y 7 años para entidades jurídicas (Art. 9 LAU).
    * **Desistimiento:** Verifica el mínimo obligatorio de 6 meses antes de permitir salidas unilaterales.
    * **Prórroga Tácita:** Extensión automática anual del contrato hasta un límite legal de 3 años (Art. 10 LAU).
    * **Actualización Anual de Renta:** Reajuste automático en la fecha de aniversario utilizando el índice oficial.
* **🇮🇨 Fiscalidad de Canarias (IGIC):** Núcleo financiero adaptado, que aplica automáticamente la **exención del IGIC (0%)** en viviendas de alquiler habitual, manteniendo el soporte para alquileres turísticos.
* **🏦 Fianzas y Garantías (ICAVI):** Validación estricta del depósito legal (exactamente 1 mes) y garantías adicionales (máximo 2 meses), con seguimiento de los depósitos oficiales en el ICAVI.
* **📊 Dashboard de Rentabilidad:** Visualización interactiva en tiempo real del margen neto (ingresos de alquiler totales menos gastos registrados) de manera consolidada y por unidad.
* **🔐 Cifrado de Contraseñas y Seguridad (AES-GCM):** Almacenamiento súper seguro mediante AES-256 en modo GCM, con derivación de clave por PBKDF2 y salting dinámico. Las contraseñas en memoria se manejan estrictamente como arrays `char[]` para evitar fugas y volcados de memoria (Garbage Collector).

---

## 🛠️ Instalación y Uso

La versión **v1.1.0-F** está pensada para ser instalada directamente en Windows sin requerir configuraciones de entorno de desarrollo:

1. **Descarga el instalador**:
   Descarga el archivo `wizardSetUp.exe` disponible en este repositorio.
2. **Ejecuta el asistente**:
   Haz doble clic sobre `wizardSetUp.exe` y sigue el asistente de instalación interactivo.
3. **Inicia la aplicación**:
   El instalador creará un acceso directo en tu Escritorio y en tu menú de Inicio con el logotipo oficial de Habitly.

---

## 🧪 Pruebas de Cumplimiento (Compliance QA Testing con JUnit 5)

Habitly cuenta con una suite completa de pruebas unitarias y de integración de cumplimiento legal migradas a **JUnit 5**.

Ejecutar la suite de pruebas JUnit 5 en entorno de desarrollo:
```powershell
.\run.bat
java -jar lib/junit-platform-console-standalone-1.10.2.jar --class-path out --scan-class-path
```

---
---

# 🏠 Habitly - Property Management System (v1.1.0-F GUI)

**Habitly** is a professional desktop application designed for comprehensive real estate asset management. With **version v1.1.0-F (Full GUI)**, the system transitions from a CLI to a **premium interactive Graphical User Interface (Dark Mode)**. It features a built-in **Legal Compliance Engine** complying with Spanish housing regulations (Law 12/2023) and lease laws (LAU), financial dashboards, and a Windows installation wizard.

---

## 🚀 Key Features (v1.1.0-F)

* **🖥️ Premium Graphical Interface (Dark Mode):** An interactive UI built on Java Swing with a modern slate-color palette. It features real-time focus highlights, navigation sidebars, and responsive tables with optimized column spacing.
* **📦 Windows Setup Wizard (`wizardSetUp.exe`):** Out-of-the-box Windows installer. It bundles a reduced Java Runtime Environment (JRE), meaning **pre-installing Java is not required** on the target computer.
* **⚖️ Legal Compliance (Law 12/2023 & LAU):** Automatically blocks any lease contract that exceeds the legal monthly rent limit established under the official **IRAV** index for stressed areas.
* **📜 LAU Lease Lifecycle:** Enforces all lease regulations:
    * **Minimum Legal Duration:** 5 years for natural persons; 7 years for companies (Art. 9 LAU).
    * **Tenant Lease Exit:** Verifies a minimum 6-month threshold before permitting unilateral exits.
    * **Tacit Extensions (Prórroga Tácita):** Automated annual contract extensions up to a maximum legal cap of 3 years (Art. 10 LAU).
    * **Annual Rent Re-adjustments:** Automates rent updates on the anniversary date.
* **🇮🇨 Canary Islands Taxation (IGIC):** Optimized financial core applying **IGIC exemption (0%)** for primary residences, keeping support for tourist rental tax calculations.
* **🏦 Security Deposits (ICAVI):** Strict validation of security deposits (exactly 1 month) and additional guarantees (max 2 months), with active tracking of official deposits with the ICAVI.
* **📊 Profit Dashboard:** Live calculation of owner net margins (total rental income minus registered housing expenses).
* **🔐 Password Encryption & Memory Safety (AES-GCM):** Data is encrypted via AES-256 in GCM mode and PBKDF2. Passwords in memory are handled strictly as `char[]` arrays to eliminate potential memory dumps.

---

## 🛠️ Installation & Usage

Version **v1.1.0-F** is designed to run directly on Windows without development environment configurations:

1. **Download the installer**:
   Download `wizardSetUp.exe` from this repository.
2. **Run the setup**:
   Double-click `wizardSetUp.exe` and follow the setup wizard steps.
3. **Launch the application**:
   The installer will place a desktop shortcut and a Start Menu icon for easy launch.

---

## 🧪 Compliance QA Testing (JUnit 5)

Habitly includes automated JUnit 5 suites that verify the compliance of lease limits, deposit caps, and extensions.

Run the JUnit 5 Compliance Audit Suite:
```powershell
.\run.bat
java -jar lib/junit-platform-console-standalone-1.10.2.jar --class-path out --scan-class-path
```

---
Developed with ❤️ by DevNaranjo