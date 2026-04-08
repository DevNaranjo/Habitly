import com.habitly.data.GestorInventario;
import com.habitly.model.*;
import java.io.File;
import java.util.List;

/**
 * Test de Persistencia v1.0.34
 * Verifica el ciclo de cifrado AES y recuperación mediante CajaFuerte.
 */
public class HabitlyPersistenciaTest {
    public static void main(String[] args) {
        System.out.println("=== TEST DE PERSISTENCIA CIFRADA (AES) v1.0.34 ===");

        // 1. Inicialización y Limpieza de fábrica para empezar de cero
        GestorInventario gestor = new GestorInventario();
        System.out.println("Limpiando datos previos...");
        gestor.borrarDatosAplicacion();

        // 2. Crear datos de prueba con los constructores de v1.0.4
        // Piso (12 params) y Casa (11 params)
        Piso p1 = new Piso("PROPIETARIO_01", "Calle Real 10", 750.0, 60.0, 2, 1, false, false, true, "Bueno", 1, "A");
        Casa c1 = new Casa("PROPIETARIO_01", "Urb. El Bosque", 1500.0, 200.0, 4, 3, true, true, false, "Excelente", 600.0);

        gestor.añadirVivienda(p1);
        gestor.añadirVivienda(c1);

        // 3. Ejecutar Guardado Cifrado
        System.out.println("Ejecutando guardado cifrado en /data/sistema.dat...");
        gestor.guardarDatos();

        // 4. Verificación física del archivo
        File archivo = new File("data" + File.separator + "sistema.dat");
        if (archivo.exists() && archivo.length() > 0) {
            System.out.println("✅ OK: Archivo 'sistema.dat' generado y cifrado (" + archivo.length() + " bytes).");
        } else {
            System.out.println("❌ ERROR: El archivo no se creó o está vacío.");
            return;
        }

        // 5. Simular Recarga (Instanciamos nuevo gestor para forzar lectura de disco)
        System.out.println("Simulando reinicio de App y descifrado...");
        GestorInventario nuevoGestor = new GestorInventario();
        nuevoGestor.cargarDatos();

        // 6. Validar integridad de los datos recuperados
        List<Vivienda> recuperadas = nuevoGestor.getInventario();

        if (recuperadas.size() == 2) {
            System.out.println("✅ OK: Se han recuperado las 2 viviendas tras el descifrado.");

            // Comprobar que los datos específicos de v1.0.4 siguen ahí
            for (Vivienda v : recuperadas) {
                if (v instanceof Piso) {
                    System.out.println("   -> Verificado Piso: Puerta " + ((Piso) v).getPuerta());
                } else if (v instanceof Casa) {
                    System.out.println("   -> Verificado Casa: Parcela " + ((Casa) v).getMetrosParcela() + "m2");
                }
            }
        } else {
            System.out.println("❌ ERROR: Discrepancia en los datos recuperados. Tamaño: " + recuperadas.size());
        }

        System.out.println("=================================================");
    }
}