import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.habitly.model.*;

public class UsuarioTest {

    static {
        System.setProperty("habitly.env", "test");
    }

    @Test
    public void testAtributosYHerenciaUsuario() {
        Inquilino inq = new Inquilino("12345678X", "Juan Prueba", "600123456", "juan@mail.com", 85, "hash", "salt");
        Propietario prop = new Propietario("B99887766", "Inmobiliaria S.L.", "912345678", "contacto@inmo.com", true, "hash", "salt");

        assertEquals(85, inq.getSolvencia());
        assertTrue(prop.isEsEmpresa());
        assertTrue(inq instanceof Usuario);
        assertTrue(prop instanceof Usuario);
        assertEquals("Juan Prueba", inq.getNombre());
        assertEquals("B99887766", prop.getDni());
    }
}