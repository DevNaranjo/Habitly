import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.habitly.data.GestorInventario;
import com.habitly.model.*;
import com.habitly.utils.InputValidator;
import java.util.List;
import java.time.LocalDate;

public class ValidadorLegalComplianceTest {

    static {
        System.setProperty("habitly.env", "test");
    }

    @Test
    public void testLimiteRentaIRAV() {
        GestorInventario motor = new GestorInventario();
        Piso v = new Piso("ID_PROPIETARIO", "Av. Marítima, LPGC", 900, 80, 3, 2, true, false, true, "Reformado", 1, "A");
        v.setLimiteMaximoIrav(950.0);

        Inquilino inq = new Inquilino("12345678X", "Juan", "123", "j@j.com", 90, "hash", "salt");
        motor.añadirUsuario(inq);

        ContratoAlquiler cIlegal = new ContratoAlquiler(v.getDireccion(), "12345678X", TipoArrendador.FISICO, 1100.0, 60, 'B');
        String respuesta = motor.formalizarContrato(cIlegal, v);
        assertTrue(respuesta.startsWith("ERROR LEGAL"));
    }

    @Test
    public void testProrrogaLegalSegunArrendador() {
        ContratoAlquiler cEmpresa = new ContratoAlquiler("Ref_A", "B12345678", TipoArrendador.JURIDICO, 1200, 60, 'A');
        assertFalse(cEmpresa.cumpleDuracionMinimaLegal());
    }

    @Test
    public void testDepositoFianzasICAVI() {
        GestorInventario motor = new GestorInventario();
        Casa v = new Casa("ID_DUEÑO", "Santa Cruz de Tenerife", 600, 50, 2, 1, false, false, false, "Bueno", 100.0);
        v.setLimiteMaximoIrav(800.0);

        Inquilino inq = new Inquilino("99999999R", "Pedro", "123", "p@p.com", 90, "hash", "salt");
        motor.añadirUsuario(inq);

        ContratoAlquiler c = new ContratoAlquiler(v.getDireccion(), "99999999R", TipoArrendador.FISICO, 700, 60, 'D');
        motor.añadirVivienda(v);
        motor.formalizarContrato(c, v);

        List<ContratoAlquiler> pendientes = motor.obtenerContratosPendientesFianza();
        assertFalse(pendientes.isEmpty());
        assertFalse(pendientes.get(0).isFianzaDepositada());
    }

    @Test
    public void testFianzaLegalYGarantias() {
        GestorInventario motor = new GestorInventario();
        Piso v = new Piso("ID_PROP", "Mesa y Lopez, LPGC", 800, 70, 2, 1, false, false, true, "Reformado", 2, "B");
        v.setLimiteMaximoIrav(900.0);
        motor.añadirVivienda(v);

        Inquilino inq = new Inquilino("12345678X", "Juan", "123", "j@j.com", 90, "hash", "salt");
        motor.añadirUsuario(inq);

        // 1. Fianza errónea
        ContratoAlquiler cFianzaErronea = new ContratoAlquiler(v.getDireccion(), "12345678X", TipoArrendador.FISICO, 800.0, 60, 'E', 700.0, 0.0);
        String res1 = motor.formalizarContrato(cFianzaErronea, v);
        assertTrue(res1.startsWith("ERROR LEGAL"));

        // 2. Garantías excesivas
        ContratoAlquiler cGarantiaErronea = new ContratoAlquiler(v.getDireccion(), "12345678X", TipoArrendador.FISICO, 800.0, 60, 'F', 800.0, 1700.0);
        String res2 = motor.formalizarContrato(cGarantiaErronea, v);
        assertTrue(res2.startsWith("ERROR LEGAL"));

        // 3. Contrato correcto
        ContratoAlquiler cCorrecto = new ContratoAlquiler(v.getDireccion(), "12345678X", TipoArrendador.FISICO, 800.0, 60, 'G', 800.0, 1600.0);
        String res3 = motor.formalizarContrato(cCorrecto, v);
        assertTrue(res3.startsWith("ÉXITO"));
    }

    @Test
    public void testActualizacionIRAV() {
        ContratoAlquiler c = new ContratoAlquiler("Calle Triana", "12345678X", TipoArrendador.FISICO, 1000.0, 60, 'A');

        // 1. Sin aniversario y sin forzar
        assertFalse(c.aplicarActualizacionIRAV(2.5, false));

        // 2. Sin aniversario pero forzando
        assertTrue(c.aplicarActualizacionIRAV(2.5, true));
        assertEquals(1025.0, c.getRentaMensual(), 0.01);

        // 3. Simular aniversario (1 año en el pasado)
        c.setRentaMensual(1000.0);
        c.setFechaFirma(LocalDate.now().minusYears(1));
        assertTrue(c.aplicarActualizacionIRAV(3.0, false));
        assertEquals(1030.0, c.getRentaMensual(), 0.01);
    }

    @Test
    public void testProrrogaTacita() {
        ContratoAlquiler c = new ContratoAlquiler("Calle Triana", "12345678X", TipoArrendador.FISICO, 1000.0, 60, 'A');
        LocalDate vtoOriginal = c.getFechaVencimiento();

        assertTrue(c.activarProrrogaTacita());
        assertTrue(c.activarProrrogaTacita());
        assertTrue(c.activarProrrogaTacita());
        assertFalse(c.activarProrrogaTacita()); // Excedido límite 3 años

        assertEquals(EstadoContrato.FINALIZADO, c.getEstado());
        assertEquals(vtoOriginal.plusYears(3), c.getFechaVencimiento());
    }

    @Test
    public void testDesistimientoInquilino() {
        ContratoAlquiler c = new ContratoAlquiler("Calle Triana", "12345678X", TipoArrendador.FISICO, 1000.0, 60, 'A');

        assertFalse(c.estaEnPeriodoDesistimiento()); // Firmado hoy

        c.setFechaFirma(LocalDate.now().minusMonths(6));
        assertTrue(c.estaEnPeriodoDesistimiento()); // Pasado 6 meses
    }

    @Test
    public void testValidacionDniModulo23() {
        // DNI Correcto (12345678Z es correcto porque 12345678 % 23 = 14 -> Z)
        assertTrue(InputValidator.validarLetraDNI("12345678Z"));
        // DNI con letra incorrecta (12345678A es incorrecto porque letra control debería ser Z)
        assertFalse(InputValidator.validarLetraDNI("12345678A"));
        // NIE Correcto (Y1234567X -> 11234567 % 23 = 10 -> X)
        assertTrue(InputValidator.validarLetraDNI("Y1234567X"));
        // NIE Incorrecto
        assertFalse(InputValidator.validarLetraDNI("Y1234567Z"));
    }
}
