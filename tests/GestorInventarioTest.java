import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.habitly.data.GestorInventario;
import com.habitly.model.*;

public class GestorInventarioTest {

    static {
        System.setProperty("habitly.env", "test");
    }

    @Test
    public void testGestorInventarioYReglasDeEliminacion() {
        GestorInventario gestor = new GestorInventario();

        Propietario p1 = new Propietario("111A", "Empresa A", "911", "a@a.com", true, "hash", "salt");
        Propietario p2 = new Propietario("111A", "Empresa B", "922", "b@b.com", false, "hash", "salt");

        assertTrue(gestor.añadirUsuario(p1));
        assertFalse(gestor.añadirUsuario(p2)); // Bloquear DNI duplicado

        Piso v1 = new Piso("111A", "Calle A", java.math.BigDecimal.valueOf(1000.0), 80.0, 2, 1, false, false, true, "Bueno", 1, "A");
        Piso v2 = new Piso("111A", "Calle B", java.math.BigDecimal.valueOf(600.0), 50.0, 1, 1, false, false, false, "Bueno", 2, "B");
        
        gestor.añadirVivienda(v1);
        gestor.añadirVivienda(v2);

        v1.registrarPago(java.math.BigDecimal.valueOf(1000.0));
        v1.registrarFactura(new Gasto("G-01", "Comunidad", java.math.BigDecimal.valueOf(150.0)));
        v1.registrarFactura(new Gasto("G-02", "Seguro", java.math.BigDecimal.valueOf(50.0)));

        java.math.BigDecimal beneficio = gestor.getBeneficioTotal("111A");
        assertEquals(800.0, beneficio.doubleValue(), 0.01);

        int idxV2 = gestor.getInventario().indexOf(v2);
        assertTrue(gestor.eliminarVivienda(idxV2)); // v2 is DISPONIBLE, can delete

        int idxV1 = gestor.getInventario().indexOf(v1);
        assertFalse(gestor.eliminarVivienda(idxV1)); // v1 is ALQUILADA, cannot delete
        gestor.borrarDatosAplicacion();
    }
}