# 🏠 Habitly - Property Management System (v1.0.34)

**Habitly** is a robust Java-based CLI application designed for real estate management, focusing on security, scalability, and a seamless user experience. It allows owners and tenants to manage properties and payments within a secure, encrypted environment.

---

## 🚀 Key Features (v1.0.34 Patch)

* **🛡️ RBAC Security (New)**: Implemented **Role-Based Access Control**. The system strictly distinguishes between **Owners**, **Tenants**, and **Guests**, blocking unauthorized roles from sensitive management menus.
* **🔐 Encrypted Persistence**: All data is secured using **AES-128 bit encryption**. The persistence engine ensures all data resides within the `/data` folder, even after a factory reset.
* **👤 Smart Onboarding**: A built-in "Setup Wizard" detects first-time users and guides them through account creation with a personalized touch ("Register as...").
* **⚠️ Enhanced UX/UI**: Pedagogical error messages in numeric and boolean inputs to prevent silent loops and guide the user during registration.
* **🔄 Iterator Pattern**: Optimized collection handling for safe and efficient data traversal, preventing concurrent modification errors.
* **🎭 Polymorphic Roles**: Distinct management for **Owners** and **Tenants**, featuring specific business logic like financial solvency indices or business entity status.
* **🧪 Built-in Testing Suite**: Includes dedicated classes (`CryptoTest`, `UserTest`, `InventoryTest`) to validate system integrity before deployment.
* **📂 Atomic Factory Reset**: Secure data wiping mechanism followed by a safe system termination to maintain database integrity.

---
## 🛠️ Installation & Usage

1. **Clone the repository**:
   ```bash
   git clone [https://github.com/TuUsuario/Habitly.git](https://github.com/TuUsuario/Habitly.git)

2. **Compile the project**: Use your favorite IDE (IntelliJ, Eclipse, or VS Code) or compile via terminal.

3. **Run the application**:
        java com.habitly.ui.Habitly

**Note**: On your first run, the system will trigger the **Setup Wizard**. You can also enter **Guest Mode** to explore the features with read-only privileges.

## 🏗️ Architecture & Patterns
The project follows clean coding standards and professional design patterns:

* **Singleton/Manager Pattern:** Centralized logic handled by GestorInventario.

* **RBAC (Role-Based Access Control):** Security layer that restricts methods and menus based on user instance types (Inheritance-based security).

* **Standardized Documentation:** Full Javadoc integration across all classes with @since tags for technical traceability.

* **Inheritance & Polymorphism:** A robust User base class with specialized Tenant and Owner implementations.

## 🔒 Security
Data integrity is the core of Habitly. The CryptoManager class handles all I/O operations, ensuring that the sistema.dat file remains a secure binary blob. For privacy compliance, **Tenants** and **Guests** are programmatically restricted from accessing user registration or sensitive profile management.

Developed with ❤️ by DevNaranjo