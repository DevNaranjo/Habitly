# 🏠 Habitly - Property Management System (v1.0.5)

**Habitly** es una aplicación CLI profesional desarrollada en Java para la gestión integral de activos inmobiliarios. En su **versión 1.0.5**, el sistema evoluciona hacia el cumplimiento legal (**Compliance**), integrando motores de validación de la Ley de Vivienda y el control de rentas en zonas tensionadas.

---

## 🚀 Key Features (v1.0.5 - Legal Compliance & Business Logic)

* **⚖️ Compliance Legal (Ley 12/2023):** Implementación del índice **IRAV**. El sistema bloquea automáticamente cualquier contrato que supere el límite legal de renta mensual establecido para la vivienda.
* **📜 Motor de Contratación LAU:** Nueva entidad `ContratoAlquiler` que valida automáticamente la duración mínima legal según el **Art. 9 de la LAU**:
    * **Personas Físicas:** Mínimo 5 años (60 meses).
    * **Personas Jurídicas:** Mínimo 7 años (84 meses).
* **🇮🇨 Fiscalidad Canaria (IGIC):** Núcleo financiero optimizado para el archipiélago, con cálculo automático del **7% de IGIC** y redondeo de precisión financiera.
* **🏦 Control de Fianzas (ICAVI):** Sistema de rastreo para detectar depósitos de fianza pendientes ante los organismos oficiales, garantizando que el propietario cumpla con sus obligaciones legales.
* **🔐 Seguridad AES-128:** Persistencia profesional mediante cifrado **AES de 128 bits**. Los datos se almacenan en un binario cifrado (`sistema.dat`), protegiendo la privacidad de propietarios e inquilinos.
* **🏗️ Modelado de Activos Especializado:** Distinción arquitectónica estricta entre **Pisos** (gestión de planta/puerta) y **Casas** (gestión de parcelas y exteriores).
* **🧪 Suite de Auditoría Legal:** Incluye el nuevo `ValidadorLegalCompliance` diseñado para auditar la integridad del sistema ante inspecciones de normativa de vivienda.

---

## 🛠️ Installation & Usage

1. **Clone the repository**:
   ```bash
   git clone [https://github.com/DevNaranjo/Habitly.git](https://github.com/DevNaranjo/Habitly.git)

2. **Compile the project**: Use your favorite IDE (IntelliJ, Eclipse, or VS Code) or compile via terminal.

3. **Run the application**:
        java com.habitly.ui.Habitly

**Note**: On your first run, the system will trigger the **Setup Wizard**. You can also enter **Guest Mode** to explore the features with read-only privileges.

---

## 🏗️ Architecture & Patterns

El proyecto sigue estándares de código limpio (*Clean Code*) y patrones de diseño profesionales:

* **Manager Pattern:** Lógica centralizada en `GestorInventario`, que ahora actúa como "Gatekeeper" legal para todas las transacciones de alquiler.
* **Persistence Layer:** Uso de `CajaFuerte` & `CryptoManager` para serialización segura y cifrado simétrico en las operaciones de E/S.
* **Hierarchy & Polymorphism:** Estructura de clases abstractas para `Vivienda` y `Usuario`, permitiendo un comportamiento dinámico basado en roles y tipos de propiedad.
* **Traceability:** Documentación **Javadoc** completa en español, con control de versiones `@version 1.0.5` y trazabilidad de cambios por autor.

---

## 🔒 Security & Legal Logic

La integridad de los datos y la seguridad jurídica son los pilares de Habitly:

1.  **Validación de Renta:** El sistema cruza en tiempo real el precio base propuesto con el límite **IRAV** almacenado en el perfil de la vivienda.
2.  **RBAC (Role-Based Access Control):** La interfaz se transforma según el rol detectado en la sesión. Solo los propietarios pueden gestionar el inventario y perfiles, mientras que los inquilinos acceden a su estado de cuenta y liquidación de suministros.
3.  **Data Protection:** Toda la base de datos se cifra antes de ser escrita en disco, cumpliendo con estándares de protección de información sensible y evitando la lectura externa de `sistema.dat`.

---

## 🧪 Testing de Cumplimiento (Compliance QA)

Para asegurar la estabilidad legal, la v1.0.5 incluye tests automáticos que verifican:
* Bloqueo de rentas abusivas (IRAV).
* Validación de prórrogas legales automáticas según arrendador.
* Detección de contratos sin fianza confirmada en el ICAVI.

---

Developed with ❤️ by DevNaranjo
