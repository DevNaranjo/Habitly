import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.habitly.data.GestorInventario;
import com.habitly.model.*;

public class HabitlyLegalTest {

    static {
        System.setProperty("habitly.env", "test");
    }

    @Test
    public void testFiscalidadIGIC() {
        Piso p = new Piso("PROP_DNI", "Calle Mayor 10", java.math.BigDecimal.valueOf(1000.0), 90.0, 3, 2, true, false, true, "Excelente", 4, "A");
        assertEquals(1000.0, p.getPrecioFinalConImpuestos().doubleValue(), 0.01);
        
        p.setEsAlquilerTuristico(true);
        assertEquals(1070.0, p.getPrecioFinalConImpuestos().doubleValue(), 0.01); // 7% IGIC
    }

    @Test
    public void testValidacionIRAV() {
        GestorInventario gestor = new GestorInventario();

        Casa casa = new Casa("PROP_DNI", "Santa Brígida", java.math.BigDecimal.valueOf(1500.0), 200.0, 5, 3, true, true, false, "Nuevo", 1000.0);
        casa.setLimiteMaximoIrav(java.math.BigDecimal.valueOf(1550.0));

        // Attempt contract with higher price
        ContratoAlquiler cIlegal = new ContratoAlquiler(casa.getDireccion(), "INQ_DNI", TipoArrendador.FISICO, java.math.BigDecimal.valueOf(1600.0), 60, 'B');
        
        // Asignar un inquilino válido para evitar error de integridad
        Inquilino inq = new Inquilino("INQ_DNI", "Juan", "123", "j@j.com", 90, "hash", "salt");
        gestor.añadirUsuario(inq);

        String resultado = gestor.formalizarContrato(cIlegal, casa).getMessage();
        assertTrue(resultado.contains("ERROR LEGAL"));
    }

    @Test
    public void testCicloVidaAlquiler() {
        GestorInventario gestor = new GestorInventario();

        Inquilino inq = new Inquilino("12345678X", "Juan Prueba", "600000000", "juan@mail.com", 90, "hash", "salt");
        gestor.añadirUsuario(inq);

        Piso piso = new Piso("B9988", "Avenida Mesa y López", java.math.BigDecimal.valueOf(800.0), 75.0, 2, 1, false, false, true, "Bueno", 3, "C");
        piso.setLimiteMaximoIrav(java.math.BigDecimal.valueOf(1000.0));
        gestor.añadirVivienda(piso);

        ContratoAlquiler contrato = new ContratoAlquiler(piso.getDireccion(), inq.getDni(), TipoArrendador.FISICO, java.math.BigDecimal.valueOf(800.0), 60, 'B');
        String res = gestor.formalizarContrato(contrato, piso).getMessage();
        
        assertEquals("ÉXITO: Contrato formalizado.", res);
        assertEquals(EstadoVivienda.ALQUILADA, piso.getEstado());
        assertNotNull(piso.getInquilino());
        assertEquals(inq.getDni(), piso.getInquilino().getDni());
    }
}