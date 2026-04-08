import com.habitly.data.GestorInventario;
import com.habitly.model.Propietario;

/**
 * Test de validación del almacenamiento y duplicados.
 */
public class GestorInventarioTest {
    public static void main(String[] args) {
        System.out.println("=== UNIT TEST: GESTOR DE INVENTARIO ===");
        GestorInventario gestor = new GestorInventario();

        Propietario p1 = new Propietario("111A", "Empresa A", 911, "a@a.com", true);
        Propietario p2 = new Propietario("111A", "Empresa B", 922, "b@b.com", false);

        // 1. Añadir el primero
        gestor.añadirUsuario(p1);

        // 2. Intentar añadir duplicado (DNI 111A)
        // Nota: Esto asume que tu método añadirUsuario devuelve false si el DNI ya existe
        boolean añadido = gestor.añadirUsuario(p2);

        if (!añadido) {
            System.out.println("El sistema bloqueó correctamente un DNI duplicado.");
        } else {
            System.out.println("ERROR: El sistema permitió duplicar un DNI.");
        }
    }
}