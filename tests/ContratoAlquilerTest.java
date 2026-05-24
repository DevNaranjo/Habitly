import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.habitly.model.ContratoAlquiler;
import com.habitly.model.TipoArrendador;

public class ContratoAlquilerTest {

    static {
        System.setProperty("habitly.env", "test");
    }

    @Test
    public void testContratoDuracionMinimaYFianzas() {
        // 1. Probar contrato para Persona Física (Mínimo 5 años / 60 meses)
        ContratoAlquiler contratoFisico = new ContratoAlquiler(
                "Calle Triana 10", "12345678X", TipoArrendador.FISICO, 800.0, 60, 'A'
        );

        // 2. Probar contrato para Persona Jurídica (Mínimo 7 años / 84 meses)
        ContratoAlquiler contratoEmpresa = new ContratoAlquiler(
                "Av. Mesa y López", "B99887766", TipoArrendador.JURIDICO, 1200.0, 60, 'B'
        );

        assertTrue(contratoFisico.cumpleDuracionMinimaLegal());
        assertFalse(contratoEmpresa.cumpleDuracionMinimaLegal());

        assertFalse(contratoFisico.isFianzaDepositada());
        contratoFisico.registrarDepositoFianza();
        assertTrue(contratoFisico.isFianzaDepositada());

        assertTrue(contratoFisico.getIdContrato().startsWith("CON-A-"));
    }
}