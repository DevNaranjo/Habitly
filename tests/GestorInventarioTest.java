import com.habitly.data.GestorInventario;
import com.habitly.model.*;

/**
 * Test de validación del almacenamiento, duplicados, beneficio total y eliminación de vivienda.
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
        boolean añadido = gestor.añadirUsuario(p2);

        if (!añadido) {
            System.out.println("El sistema bloqueó correctamente un DNI duplicado.");
        } else {
            System.out.println("ERROR: El sistema permitió duplicar un DNI.");
        }

        // 3. Test de beneficio total (Feature 1) y eliminación de vivienda (Feature 3)
        System.out.println("\n--- PRUEBAS DE FUNCIONALIDADES (FASE 2) ---");
        
        Piso v1 = new Piso("111A", "Calle A", 1000.0, 80.0, 2, 1, false, false, true, "Bueno", 1, "A");
        Piso v2 = new Piso("111A", "Calle B", 600.0, 50.0, 1, 1, false, false, false, "Bueno", 2, "B");
        
        gestor.añadirVivienda(v1);
        gestor.añadirVivienda(v2);

        // Registrar pagos e ingresos en v1
        v1.registrarPago(1000.0); // Esto la cambia a ALQUILADA
        v1.registrarFactura(new Gasto("G-01", "Comunidad", 150.0));
        v1.registrarFactura(new Gasto("G-02", "Seguro", 50.0));

        double beneficio = gestor.getBeneficioTotal("111A");
        if (beneficio == 800.0) {
            System.out.println("? OK: getBeneficioTotal calcula ingresos - gastos correctamente (800.0€).");
        } else {
            System.out.println("ERROR: getBeneficioTotal devolvió " + beneficio + "€ (esperado: 800.0€).");
        }

        // v2 está DISPONIBLE, debería eliminarse con éxito
        int idxV2 = gestor.getInventario().indexOf(v2);
        boolean eliminadaV2 = gestor.eliminarVivienda(idxV2);
        
        if (eliminadaV2 && gestor.getInventario().size() == 1) {
            System.out.println("? OK: eliminarVivienda eliminó una vivienda DISPONIBLE correctamente.");
        } else {
            System.out.println("ERROR: eliminarVivienda falló al eliminar vivienda DISPONIBLE.");
        }

        // v1 está ALQUILADA, no debería poder eliminarse
        int idxV1 = gestor.getInventario().indexOf(v1);
        boolean eliminadaV1 = gestor.eliminarVivienda(idxV1);
        
        if (!eliminadaV1 && gestor.getInventario().size() == 1) {
            System.out.println("? OK: eliminarVivienda bloqueó correctamente la eliminación de una vivienda ALQUILADA.");
        } else {
            System.out.println("ERROR: eliminarVivienda permitió eliminar una vivienda ALQUILADA.");
        }
    }
}