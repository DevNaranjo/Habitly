import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.habitly.data.GestorInventario;
import com.habitly.model.*;
import com.habitly.services.*;
import com.habitly.utils.Result;

public class ServiceSecurityTest {

    static {
        System.setProperty("habitly.env", "test");
    }

    @Test
    public void testAuthorizationRestrictions() {
        GestorInventario gestor = new GestorInventario();
        ViviendaService viviendaService = new ViviendaService(gestor);
        ContratoService contratoService = new ContratoService(gestor);

        // 1. Sin usuario identificado (null)
        assertFalse(viviendaService.registrarPiso("12345678Z", "Calle A", 500, 50, 2, 1, false, false, false, "Buena", 1, "A", false));
        assertFalse(viviendaService.registrarGastoSuministro(null, "Luz", 100));

        // 2. Con usuario Invitado (Guest DNI)
        Propietario invitado = new Propietario(com.habitly.config.AppConfig.GUEST_ID, "Invitado", "0", "guest@habitly.com", false, "", "");
        gestor.setUsuarioIdentificado(invitado);

        assertFalse(viviendaService.registrarPiso("12345678Z", "Calle A", 500, 50, 2, 1, false, false, false, "Buena", 1, "A", false));
        assertFalse(viviendaService.eliminarVivienda(null));

        // 3. Propietario legítimo vs Propietario ajeno
        Propietario prop1 = new Propietario("48484848L", "Prop 1", "123", "a@a.com", false, "hash", "salt");
        Propietario prop2 = new Propietario("71234567L", "Prop 2", "123", "b@b.com", false, "hash", "salt");

        gestor.setUsuarioIdentificado(prop1);
        gestor.añadirUsuario(prop1);
        gestor.añadirUsuario(prop2);

        // prop1 registra un piso para sí mismo
        assertTrue(viviendaService.registrarPiso("48484848L", "Calle Prop1", 600, 60, 3, 2, true, false, true, "Excelente", 2, "B", false));

        Vivienda piso1 = gestor.getInventario().get(0);
        assertEquals("48484848L", piso1.getIdPropietario());

        // prop2 se identifica e intenta modificar el piso de prop1
        gestor.setUsuarioIdentificado(prop2);
        assertFalse(viviendaService.actualizarPiso((Piso)piso1, "Calle Cambiada", 700, 60, 3, 2, true, false, true, "Excelente", 2, "B", false));
        assertNotEquals("Calle Cambiada", piso1.getDireccion());
    }
}
