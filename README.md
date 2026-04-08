# 🏠 Habitly - Property Management System (v1.0.5)

**Habitly** is a professional Java-based CLI application designed for comprehensive real estate asset management. In **version 1.0.5**, the system evolves towards **Legal Compliance**, integrating validation engines for Housing Laws and rent control in stressed markets.

---

## 🚀 Key Features (v1.0.5 - Legal Compliance & Business Logic)

* **⚖️ Legal Compliance (Law 12/2023):** Implementation of the **IRAV index**. The system automatically blocks any contract that exceeds the legal monthly rent limit established for the property.
* **📜 LAU Contract Engine:** New `ContratoAlquiler` entity that automatically validates the minimum legal duration according to **Art. 9 of the LAU**:
    * **Natural Persons:** Minimum 5 years (60 months).
    * **Legal Entities (Companies):** Minimum 7 years (84 months).
* **🇮🇨 Canary Islands Taxation (IGIC):** Optimized financial core for the archipelago, featuring automatic **7% IGIC** calculation and financial-grade precision rounding.
* **🏦 Security Deposit Control (ICAVI):** Tracking system to detect pending security deposits with official bodies, ensuring the owner meets all legal obligations.
* **🔐 AES-128 Persistence:** Professional-grade persistence via **128-bit AES encryption**. Data is stored in an encrypted binary file (`sistema.dat`), protecting owner and tenant privacy.
* **🏗️ Specialized Asset Modeling:** Strict architectural distinction between **Pisos** (floor/door management) and **Casas** (plot and outdoor management).
* **🧪 Legal Audit Suite:** Includes the new `ValidadorLegalCompliance` designed to audit system integrity against housing regulation inspections.

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

The project follows **Clean Code** standards and professional design patterns:

* **Manager Pattern:** Centralized logic handled by `GestorInventario`, which now acts as a legal "Gatekeeper" for all rental transactions.
* **Persistence Layer:** Uses `CajaFuerte` & `CryptoManager` for secure serialization and symmetric encryption for I/O operations.
* **Hierarchy & Polymorphism:** Abstract class structure for `Vivienda` and `Usuario`, allowing dynamic behavior based on roles and property types.
* **Traceability:** Full documentation using **Javadoc**, with `@version 1.0.5` control and author-based change traceability.

---

## 🔒 Security & Legal Logic

Data integrity and legal certainty are the core pillars of Habitly:

1.  **Rent Validation:** The system performs real-time checks between the proposed base price and the **IRAV limit** stored in the property profile.
2.  **RBAC (Role-Based Access Control):** The interface adapts based on the detected session role. Only owners can manage inventory and profiles, while tenants access their account statements and utility bill settlements.
3.  **Data Protection:** The entire database is encrypted before being written to disk, complying with sensitive information protection standards and preventing external reading of `sistema.dat`.

---

## 🧪 Compliance QA Testing

To ensure legal stability, v1.0.5 includes automated tests that verify:
* Blocking of abusive rents (IRAV).
* Validation of automatic legal renewals based on the landlord type.
* Detection of contracts without confirmed security deposits in ICAVI.

---

Developed with ❤️ by DevNaranjo