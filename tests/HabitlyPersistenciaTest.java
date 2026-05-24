import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.habitly.data.GestorInventario;
import com.habitly.model.*;
import com.habitly.config.AppConfig;
import java.io.File;
import java.util.List;

public class HabitlyPersistenciaTest {

    static {
        System.setProperty("habitly.env", "test");
    }

    @Test
    public void testPersistenciaCifrada() {
        GestorInventario gestor = new GestorInventario();
        gestor.borrarDatosAplicacion();

        Piso p1 = new Piso("PROPIETARIO_01", "Calle Real 10", 750.0, 60.0, 2, 1, false, false, true, "Bueno", 1, "A");
        Casa c1 = new Casa("PROPIETARIO_01", "Urb. El Bosque", 1500.0, 200.0, 4, 3, true, true, false, "Excelente", 600.0);

        gestor.añadirVivienda(p1);
        gestor.añadirVivienda(c1);

        gestor.guardarDatos();

        File archivo = new File(AppConfig.getFullFilePath());
        assertTrue(archivo.exists());
        assertTrue(archivo.length() > 0);

        GestorInventario nuevoGestor = new GestorInventario();
        nuevoGestor.cargarDatos();

        List<Vivienda> recuperadas = nuevoGestor.getInventario();
        assertEquals(2, recuperadas.size());

        for (Vivienda v : recuperadas) {
            if (v instanceof Piso) {
                assertEquals("A", ((Piso) v).getPuerta());
            } else if (v instanceof Casa) {
                assertEquals(600.0, ((Casa) v).getMetrosParcela(), 0.01);
            }
        }
        nuevoGestor.borrarDatosAplicacion();
    }
}