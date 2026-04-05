## [1.0.33] - 2026-04-05
### 🔐 Parche de Seguridad y Flujo de Acceso

### Añadido ✨
- **Sistema de Login:** Nueva opción en el menú de inicio para usuarios existentes mediante validación de DNI.
- **Persistencia de Sesión:** El sistema ahora reconoce al usuario activo durante toda la ejecución.

### Cambios 🛠️
- **Restricción de Privilegios (GUEST):** Se ha limitado la capacidad de los invitados. El perfil `GUEST-001` ya no tiene permisos para:
  - Registrar nuevos usuarios.
  - Eliminar registros existentes.
- **Menú de Bienvenida:** Reorganización del "Setup Wizard" para incluir la vía de acceso rápido (Login).

### Notas de Implementación 📝
- Se requiere la creación de un atributo `usuarioIdentificado` en la lógica de control para gestionar los permisos por perfil.