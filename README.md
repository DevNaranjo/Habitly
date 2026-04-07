# 🏠 Habitly - Property Management System (v1.0.4)

**Habitly** is a robust Java-based CLI application designed for real estate management, focusing on security, scalability, and a seamless user experience. It allows owners and tenants to manage properties and payments within a secure, encrypted environment.

---

## 🚀 Key Features (v1.0.4 - Economy & Synchronization)

* **💰 Integrated Economy Core**: Full support for financial management including **IGIC (7%)** tax calculation, monthly rental balance, and manual expense tracking (Electricity, Water, Taxes).
* **🛡️ Advanced RBAC Security**: Improved **Role-Based Access Control**. The system dynamically adapts the UI based on the logged-in user: **Owners** manage assets and profiles, while **Tenants** track their personal debt and bills.
* **🔐 AES-128 Persistence**: Professional encryption engine using **AES-128 bit**. Data is stored as an encrypted binary blob (`sistema.dat`), ensuring privacy even if the file is accessed externally.
* **🏗️ Synchronized Property Models**: Precise architectural distinction between **Pisos** (12 parameters: floor/door logic) and **Casas** (11 parameters: plot size logic), fully integrated with the persistence engine.
* **👤 Smart Session Management**: DNI-based login system that persists the user's identity throughout the execution, enabling automated data filtering and personalized dashboard views.
* **🧪 Full Testing Suite (QA)**: Includes 4 dedicated test suites (`Model`, `Logic`, `Persistence`, `User`) to ensure 100% stability in financial calculations and cryptographic operations.
* **🎭 Multi-role UI**: Polymorphic main menu that toggles available operations based on whether you are a **Propietario**, **Inquilino**, or **Invitado**.

---

## 🛠️ Installation & Usage

1. **Clone the repository**:
   ```bash
   git clone [https://github.com/TuUsuario/Habitly.git](https://github.com/TuUsuario/Habitly.git)

2. **Compile the project**: Use your favorite IDE (IntelliJ, Eclipse, or VS Code) or compile via terminal.

3. **Run the application**:
        java com.habitly.ui.Habitly

**Note**: On your first run, the system will trigger the **Setup Wizard**. You can also enter **Guest Mode** to explore the features with read-only privileges.

---

## 🏗️ Architecture & Patterns
The project follows clean coding standards and professional design patterns:

* **Manager Pattern:** Centralized logic handled by `GestorInventario` for global state management and user session tracking.
* **Persistence Layer:** `CajaFuerte` & `CryptoManager` handle secure serialization and AES-128 encryption for all data I/O.
* **Inheritance & Polymorphism:** A hierarchical model structure where `Vivienda` acts as the base for specialized property types, and `Usuario` branches into specific roles.
* **Standardized Documentation:** Full **Javadoc** integration across all classes with `@since` and `@version` tags for technical traceability.

---

## 🔒 Security & Economy Logic
Data integrity and financial accuracy are the core pillars of Habitly:

1. **Financial Logic:** Expenses are automatically linked to specific property IDs and verified against the owner's DNI. The system handles partial payments and balance calculations in real-time.
2. **Access Control:** Sensitive methods such as `registrarUsuarioMenu`, `eliminarUsuario`, or `aplicarIPC` are strictly blocked for non-owner roles using `instanceof` validation.
3. **Data Protection:** All sensitive information is encrypted before hitting the disk, preventing unauthorized reading of the system database.

---

Developed with ❤️ by DevNaranjo
