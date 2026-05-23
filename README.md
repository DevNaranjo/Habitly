# 🏠 Habitly - Sistema de Gestión Inmobiliaria (v1.0)

**Habitly** es una aplicación CLI (Interfaz de Línea de Comandos) profesional basada en Java diseñada para la gestión integral de activos inmobiliarios. Con la versión **1.0**, el sistema se consolida implementando un motor completo de **Cumplimiento Legal (Compliance)** ajustado a la legislación española (Ley 12/2023 por el Derecho a la Vivienda) y la LAU, incorporando cálculos fiscales, dashboards de beneficios y una suite de pruebas automatizadas.

---

## 🚀 Características Principales (v1.0)

* **⚖️ Cumplimiento Legal (Ley 12/2023 y LAU 2026):** Motor de validación de contratos que bloquea automáticamente cualquier alquiler que supere el límite legal de renta mensual establecido por el índice **IRAV** para zonas tensionadas.
* **📜 Ciclo de Vida del Contrato:** La entidad `ContratoAlquiler` fuerza automáticamente la normativa:
    * **Duración Mínima:** 5 años para personas físicas y 7 años para entidades jurídicas (Art. 9 LAU).
    * **Desistimiento:** Verifica el mínimo obligatorio de 6 meses antes de permitir salidas unilaterales.
    * **Prórroga Tácita:** Extensión automática anual del contrato hasta un límite legal de 3 años (Art. 10 LAU).
    * **Actualización Anual de Renta:** Reajuste automático en la fecha de aniversario utilizando el índice oficial.
* **🇮🇨 Fiscalidad de Canarias (IGIC):** Núcleo financiero adaptado, que aplica automáticamente la **exención del IGIC (0%)** en viviendas de alquiler habitual, manteniendo el soporte para alquileres turísticos.
* **🏦 Fianzas y Garantías (ICAVI):** Validación estricta del depósito legal (exactamente 1 mes) y garantías adicionales (máximo 2 meses), con seguimiento en tiempo real de los depósitos pendientes de registro oficial en el ICAVI.
* **📊 Dashboard de Beneficios del Propietario:** Cálculo en tiempo real del margen neto (ingresos de alquiler totales menos gastos registrados) de manera consolidada y por unidad.
* **🔐 Persistencia Cifrada de Grado Militar (AES-GCM):** Almacenamiento súper seguro mediante AES-256 en modo GCM, con derivación de clave por PBKDF2 y salting dinámico. Los datos se guardan en un archivo binario validado contra manipulaciones externas, protegiendo absolutamente la privacidad.

## 🛠️ Instalación y Uso

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/DevNaranjo/Habitly.git
   ```

2. **Compilar el proyecto (JDK 17+ u OpenJDK 25)**:
   ```bash
   javac -d out -sourcepath src src/com/habitly/ui/Habitly.java src/com/habitly/model/*.java src/com/habitly/data/*.java src/com/habitly/config/*.java
   javac -d out -cp out -sourcepath tests tests/*.java
   ```

3. **Ejecutar la aplicación**:
   ```bash
   java -cp out com.habitly.ui.Habitly
   ```

*Nota: Al ejecutarse por primera vez, el asistente inicial te guiará para registrar el primer administrador. También dispone de un **Modo Invitado (Sandbox)** para explorar de forma segura y sin dejar rastro en disco.*

> [!WARNING]
> **Solución de problemas (Troubleshooting):** Si al ejecutar el proyecto recibes el error `UnsupportedClassVersionError`, significa que la versión global de Java de tu sistema es demasiado antigua (ej. Java 8). Para solucionarlo, asegúrate de actualizar tus variables de entorno `PATH` para que apunten a tu JDK 17 o superior.

## 🧪 Pruebas de Cumplimiento (Compliance QA Testing)

Habitly v1.0 cuenta con una suite completa (`ValidadorLegalCompliance`) que audita en tiempo real que todas las reglas legales sobre índices de renta, fianza, renovaciones y duraciones mínimas se respeten en el sistema.

Ejecutar la auditoría legal interactiva:
```bash
java -cp out com.habitly.test.ValidadorLegalCompliance
```

---
---

# 🏠 Habitly - Property Management System (v1.0)

**Habitly** is a professional Java-based CLI application designed for comprehensive real estate asset management. With **version 1.0**, the system reaches a major milestone by establishing a full **Legal Compliance Engine** adhering to Spanish housing rules (Ley 12/2023 por el Derecho a la Vivienda) and lease regulations (LAU), featuring tax adaptations, profit dashboards, and automated compliance testing.

---

## 🚀 Key Features (v1.0)

* **⚖️ Legal Compliance (Law 12/2023 & LAU 2026):** Fully updated contract validation engines. The system automatically blocks any lease contract that exceeds the legal monthly rent limit established under the **IRAV** index for the property.
* **📜 LAU Contract Lifecycle:** The `ContratoAlquiler` entity automatically enforces:
    * **Minimum Legal Duration:** 5 years for natural persons; 7 years for legal entities/companies (Art. 9 LAU).
    * **Tenant Lease Exit:** Verifies a minimum 6-month threshold before permitting unilateral lease exits.
    * **Tacit Extensions (Prórroga Tácita):** Automated annual contract extensions up to a maximum legal cap of 3 years (Art. 10 LAU).
    * **Annual Rent Re-adjustments:** Automates rent updates on the anniversary date of the contract.
* **🇮🇨 Canary Islands Taxation (IGIC):** Optimized financial core complying with local tax regulations, recognizing that residential rental for primary residence is **exempt from IGIC** (0%), while keeping support for tourist rental tax calculations.
* **🏦 Security Deposit & Guarantees (ICAVI):** Strict validation of security deposits (exactly 1 month) and additional guarantees (max 2 months), with active tracking of pending official deposits with the ICAVI.
* **📊 Owner Profit Dashboard:** Live calculation of net margins (total rental income minus registered housing expenses).
* **🔐 Military-Grade AES-GCM Persistence:** Professional-grade persistence via **AES-256 in GCM mode**, featuring PBKDF2 key derivation and dynamic salting. Data is stored in a tamper-proof encrypted binary file, strictly protecting user privacy.

## 🛠️ Installation & Usage

1. **Clone the repository**:
   ```bash
   git clone https://github.com/DevNaranjo/Habitly.git
   ```

2. **Compile the project**:
   ```bash
   javac -d out -sourcepath src src/com/habitly/ui/Habitly.java src/com/habitly/model/*.java src/com/habitly/data/*.java src/com/habitly/config/*.java
   javac -d out -cp out -sourcepath tests tests/*.java
   ```

3. **Run the application**:
   ```bash
   java -cp out com.habitly.ui.Habitly
   ```

*Note: On your first run, the Setup Wizard will guide you. You can also enter the sandboxed **Guest Mode** to explore features securely with read-only privileges.*

> [!WARNING]
> **Troubleshooting:** If you receive an `UnsupportedClassVersionError` upon running the application, it means your system's global Java version is outdated (e.g. Java 8). To fix this, ensure your `PATH` environment variable points to your JDK 17 or newer installation.

## 🧪 Compliance QA Testing

To ensure legal stability, v1.0 includes automated suites (such as `ValidadorLegalCompliance`) that verify the blocking of abusive rents, minimum duration limits, security deposit capping, and tacit extension rules.

Run the Compliance Audit Suite:
```bash
java -cp out com.habitly.test.ValidadorLegalCompliance
```

---
Developed with ❤️ by DevNaranjo