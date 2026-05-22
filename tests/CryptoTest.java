import com.habitly.data.CryptoManager;
import java.nio.charset.StandardCharsets;

/**
 * Test de integridad para el motor de cifrado AES-128 de Habitly v1.0.6.
 */
public class CryptoTest {
    public static void main(String[] args) {
        System.out.println("=== UNIT TEST: CRYPTO MANAGER ===");

        try {
            String original = "Datos de prueba Habitly 2026";
            byte[] entrada = original.getBytes(StandardCharsets.UTF_8);

            // 1. Probar Cifrado
            byte[] cifrado = CryptoManager.cifrar(entrada);
            System.out.println("-> Cifrado completado con éxito.");

            // 2. Probar Descifrado
            byte[] descifrado = CryptoManager.descifrar(cifrado);
            String resultado = new String(descifrado, StandardCharsets.UTF_8);

            // 3. Verificación
            if (original.equals(resultado)) {
                System.out.println("RESULTADO: El motor AES es estable y simétrico.");
            } else {
                System.out.println("RESULTADO: Error de integridad de datos.");
            }

        } catch (Exception e) {
            System.out.println("ERROR: El sistema de cifrado ha fallado.");
            e.printStackTrace();
        }
    }
}