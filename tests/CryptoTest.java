import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.habitly.data.CryptoManager;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CryptoTest {

    static {
        System.setProperty("habitly.env", "test");
    }

    @Test
    public void testCifradoGuardarYLeer() throws Exception {
        String testFile = "data/test_cifrado.dat";
        Files.deleteIfExists(Paths.get(testFile));
        
        String data = "Datos de prueba Habitly 2026";
        CryptoManager.guardarObjetoCifrado(data, testFile);
        
        assertTrue(Files.exists(Paths.get(testFile)));
        assertTrue(Files.size(Paths.get(testFile)) > 0);
        
        Object read = CryptoManager.leerObjetoCifrado(testFile);
        assertEquals(data, read);
        
        Files.deleteIfExists(Paths.get(testFile));
    }

    @Test
    public void testPasswordHashing() throws Exception {
        String password = "MiSuperPassword123";
        byte[] salt = CryptoManager.generarSalt();
        char[] passwordChars = password.toCharArray();
        String hash1 = CryptoManager.hashPassword(passwordChars, salt);
        String hash2 = CryptoManager.hashPassword(passwordChars, salt);
        
        assertEquals(hash1, hash2);
        
        byte[] salt2 = CryptoManager.generarSalt();
        String hash3 = CryptoManager.hashPassword(passwordChars, salt2);
        assertNotEquals(hash1, hash3);
        
        String saltHex = CryptoManager.bytesToHex(salt);
        byte[] saltDecoded = CryptoManager.hexToBytes(saltHex);
        assertArrayEquals(salt, saltDecoded);
    }
}