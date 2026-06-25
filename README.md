# 🏠 Habitly - Property Management System (v1.1.5 GUI)

**Habitly** is a professional desktop application designed for comprehensive real estate asset management. With version **v1.1.5 (Full GUI)**, the system features a built-in **Legal Compliance Engine** complying with Spanish housing regulations (Law 12/2023) and lease laws (LAU), financial dashboards, and a Windows installation wizard.

---

## 🚀 Key Features (v1.1.5)

* 🖥️ **Premium Graphical Interface (Dark Mode):** An interactive UI built on Java Swing with a modern slate-color palette. It features real-time focus highlights, navigation sidebars, and responsive tables with optimized column spacing.
* 📦 **Windows Setup Wizard (`wizardSetUp.exe`):** Out-of-the-box Windows installer. It bundles a reduced Java Runtime Environment (JRE), meaning **pre-installing Java is not required** on the target computer to run the app.
* ⚖️ **Legal Compliance (Law 12/2023 & LAU):** Automatically blocks any lease contract that exceeds the legal monthly rent limit established under the official **IRAV** index for stressed areas.
* 📜 **LAU Lease Lifecycle:** Enforces all lease regulations:
    * **Minimum Legal Duration:** 5 years for natural persons; 7 years for companies (Art. 9 LAU).
    * **Tenant Lease Exit:** Verifies a minimum 6-month threshold before permitting unilateral exits.
    * **Tacit Extensions (Prórroga Tácita):** Automated annual contract extensions up to a maximum legal cap of 3 years (Art. 10 LAU).
    * **Annual Rent Re-adjustments:** Automates rent updates on the anniversary date.
* 🇮🇨 **Canary Islands Taxation (IGIC):** Optimized financial core applying **IGIC exemption (0%)** for primary residences, keeping support for tourist rental tax calculations.
* 🏦 **Security Deposits (ICAVI):** Strict validation of security deposits (exactly 1 month) and additional guarantees (max 2 months), with active tracking of official deposits with the ICAVI.
* 📊 **Profit Dashboard:** Live calculation of owner net margins (total rental income minus registered housing expenses).
* 🔐 **Password Encryption & Memory Safety (AES-GCM):** Data is encrypted via AES-256 in GCM mode and PBKDF2. Passwords in memory are handled strictly as `char[]` arrays to eliminate potential memory dumps.

---

## 🛠️ Installation & Usage

Version **v1.1.5** is designed to run directly on Windows without development environment configurations:

1. **Download the installer:** Download `wizardSetUp.exe` from this repository.
2. **Run the setup:** Double-click `wizardSetUp.exe` and follow the setup wizard steps.
3. **Launch the application:** The installer will place a desktop shortcut and a Start Menu icon for easy launch.

---

Developed with ❤️ by DevNaranjo