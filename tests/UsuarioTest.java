import com.habitly.model.*;

/**
 * Test de Integridad de Usuarios v1.0.6
 * Verifica la jerarquía: Usuario -> Inquilino / Propietario
 */
public class UsuarioTest {
    public static void main(String[] args) {
        System.out.println("=== UNIT TEST: MODELO DE USUARIOS v1.0.6 ===");

        // 1. Crear un Inquilino (DNI, Nombre, Teléfono, Email, Solvencia)
        Inquilino inq = new Inquilino("12345678X", "Juan Prueba", 600123456, "juan@mail.com", 85);

        // 2. Crear un Propietario (DNI, Nombre, Teléfono, Email, esEmpresa)
        Propietario prop = new Propietario("B99887766", "Inmobiliaria S.L.", 912345678, "contacto@inmo.com", true);

        // --- PRUEBA 1: Verificación de Atributos Propios ---
        if (inq.getSolvencia() == 85) {
            System.out.println("✅ OK: Solvencia de Inquilino asignada correctamente.");
        } else {
            System.out.println("❌ ERROR: Fallo en el valor de solvencia.");
        }

        if (prop.isEsEmpresa()) {
            System.out.println("✅ OK: Atributo 'Es Empresa' de Propietario verificado.");
        } else {
            System.out.println("❌ ERROR: Fallo en el atributo de empresa.");
        }

        // --- PRUEBA 2: Verificación de Herencia (Polimorfismo) ---
        // Comprobamos si los objetos "son" instancias de la clase padre Usuario
        if (inq instanceof Usuario && prop instanceof Usuario) {
            System.out.println("✅ OK: Herencia confirmada (Ambos heredan de Usuario).");
        } else {
            System.out.println("❌ ERROR: La jerarquía de clases está rota.");
        }

        // --- PRUEBA 3: Integridad de Datos del Padre ---
        if (inq.getNombre().equals("Juan Prueba") && prop.getDni().equals("B99887766")) {
            System.out.println("✅ OK: Datos base (Nombre/DNI) recuperados correctamente.");
        } else {
            System.out.println("❌ ERROR: Los datos del constructor super() no se guardaron.");
        }

        System.out.println("\n--- RESUMEN DE PERFILES ---");
        System.out.println("Ficha Inquilino: " + inq.getNombre() + " | Score: " + inq.getSolvencia());
        System.out.println("Ficha Propietario: " + prop.getNombre() + " | Empresa: " + (prop.isEsEmpresa() ? "SÍ" : "NO"));
        System.out.println("=============================================");
    }
}