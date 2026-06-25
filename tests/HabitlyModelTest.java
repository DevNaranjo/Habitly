import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.habitly.model.*;

public class HabitlyModelTest {

    static {
        System.setProperty("habitly.env", "test");
    }

    @Test
    public void testModelosInmobiliarios() {
        String dniProp = "12345678A";

        Piso miPiso = new Piso(dniProp, "Calle Mayor 5", java.math.BigDecimal.valueOf(800.0), 75.0, 3, 1,
                false, false, true, "Bueno", 2, "B");

        Casa miCasa = new Casa(dniProp, "Urb. El Pinar 12", java.math.BigDecimal.valueOf(1200.0), 150.0, 4, 2,
                true, true, false, "Excelente", 500.0);

        assertEquals(dniProp, miPiso.getIdPropietario());
        assertEquals(1200.0, miCasa.getPrecioBase().doubleValue(), 0.01);
        assertEquals(800.0, miPiso.getPrecioFinalConImpuestos().doubleValue(), 0.01); // Exento de IGIC
        assertEquals(2, miPiso.getPlanta());
        assertEquals("B", miPiso.getPuerta());
        assertEquals(500.0, miCasa.getMetrosParcela(), 0.01);
        assertNotNull(miPiso.getHistorialGastos());
        assertTrue(miPiso.getHistorialGastos().isEmpty());
    }
}