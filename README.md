# 🏠 Habitly - Property Management System (v1.0.3)

**Habitly** is a robust Java-based CLI application designed for real estate management, focusing on security, scalability, and a seamless user experience. It allows owners and tenants to manage properties and payments within a secure, encrypted environment.

---

## 🚀 Key Features (v1.0.3)

* **🛡️ Encrypted Persistence**: All data is secured using **AES-128 bit encryption**. No plain text is ever stored on the disk, ensuring GDPR-like standards for user privacy.
* **👤 Smart Onboarding**: A built-in "Setup Wizard" detects first-time users and guides them through account creation with a personalized touch ("Register as...").
* **🔄 Iterator Pattern**: Optimized collection handling for safe and efficient data traversal, preventing concurrent modification errors.
* **🎭 Polymorphic Roles**: Distinct management for **Owners** and **Tenants**, featuring specific business logic like financial solvency indices or business entity status.
* **🧪 Built-in Testing Suite**: Includes dedicated classes (`CryptoTest`, `UserTest`, `InventoryTest`) to validate system integrity before deployment.
* **📊 Professional UI**: Clean and aligned data tables using `System.out.printf` for a high-quality CLI experience.

---

## 🛠️ Installation & Usage

1. **Clone the repository**:
   ```bash
   git clone [https://github.com/TuUsuario/Habitly.git](https://github.com/TuUsuario/Habitly.git)

2. Compile the project: Use your favorite IDE (IntelliJ, Eclipse, or VS Code) or compile via terminal.

3. Run the application:
        java com.habitly.ui.Habitly

**Note**: On your first run, the system will trigger the **Setup Wizard**. You can also enter **Guest Mode** to explore the features without saving data to the encrypted file.

## 🏗️ Architecture & Patterns
The project follows clean coding standards and professional design patterns:

*  **Singleton/Manager Pattern:** Centralized logic handled by GestorInventario.

* **Iterator Pattern:** Used for listing users without exposing internal collection structures (Encapsulation).

* **Inheritance & Polymorphism:** A robust User base class with specialized Tenant and Owner implementations.

🔒 Security
Data integrity is the core of Habitly. The CryptoManager class handles all I/O operations, ensuring that the sistema.dat file remains a secure binary blob that cannot be read by external text editors.

Developed with ❤️ by *DevNaranjo*