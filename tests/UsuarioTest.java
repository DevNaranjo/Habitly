import com.habitly.model.*;

/**
 * Test para verificar la creación de perfiles y polimorfismo.
 */
public class UsuarioTest {
    public static void main(String[] args) {
        System.out.println("=== UNIT TEST: MODELO DE USUARIOS ===");

        // Crear un inquilino de prueba
        Inquilino inq = new Inquilino("12345678X", "Juan Prueba", 600000000, "test@mail.com", 85);

        // 1. Verificar Solvencia
        if (inq.getSolvencia() == 85) {
            System.out.println("Solvencia asignada correctamente.");
        } else {
            System.out.println("Error en el valor de solvencia.");
        }

        // 2. Verificar Polimorfismo (Toque visual)
        if (inq instanceof Usuario) {
            System.out.println("Herencia de Usuario confirmada.");
        }

        System.out.println("Ficha generada: " + inq.getNombre() + " | Score: " + inq.getSolvencia());
    }
}