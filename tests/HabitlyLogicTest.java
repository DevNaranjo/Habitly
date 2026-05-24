import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.habitly.model.*;
import java.util.List;

public class HabitlyLogicTest {

    static {
        System.setProperty("habitly.env", "test");
    }

    @Test
    public void testLogicaGastosYBalances() {
        Propietario prop = new Propietario("11122233X", "Luis Dueño", "600111222", "luis@mail.com", false, "hash", "salt");
        Inquilino inq = new Inquilino("44455566Y", "Ana Inquilina", "600444555", "ana@mail.com", 95, "hash", "salt");

        Piso piso = new Piso(prop.getDni(), "Calle Mayor 10, 2B", 900.0, 85.0, 3, 1,
                false, false, true, "Excelente", 2, "B");

        piso.setInquilino(inq);
        piso.setEstado(EstadoVivienda.ALQUILADA);

        piso.registrarFactura(new Gasto("LUZ-ABRIL", "Factura Electricidad", 120.0));
        piso.registrarFactura(new Gasto("AGUA-ABRIL", "Factura Agua", 30.0));

        double balanceEsperado = 1050.0;
        assertEquals(balanceEsperado, piso.calcularBalanceTotalPendiente(), 0.01);

        piso.registrarPago(500.0);
        assertEquals(400.0, piso.getPendienteDePago(), 0.01);

        List<Gasto> pendientes = piso.getListaGastosPendientes();
        assertEquals(2, pendientes.size());
    }
}