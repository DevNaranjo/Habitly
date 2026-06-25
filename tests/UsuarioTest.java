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

    @Test
    public void testAdministradorModel() {
        Propietario admin = new Propietario("87654321Y", "Admin Test", "600987654", "admin@mail.com", false, "hash", "salt");
        admin.setEsAdministrador(true);
        assertTrue(admin instanceof Usuario);
        assertTrue(admin.isEsAdministrador());
        assertEquals("PROPIETARIO", admin.getTipoUsuario());
        assertEquals("Admin Test", admin.getNombre());
    }
}