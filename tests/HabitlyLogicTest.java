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

        Piso piso = new Piso(prop.getDni(), "Calle Mayor 10, 2B", java.math.BigDecimal.valueOf(900.0), 85.0, 3, 1,
                false, false, true, "Excelente", 2, "B");

        piso.setInquilino(inq);
        piso.setEstado(EstadoVivienda.ALQUILADA);

        piso.registrarFactura(new Gasto("LUZ-ABRIL", "Factura Electricidad", java.math.BigDecimal.valueOf(120.0)));
        piso.registrarFactura(new Gasto("AGUA-ABRIL", "Factura Agua", java.math.BigDecimal.valueOf(30.0)));

        double balanceEsperado = 1050.0;
        assertEquals(balanceEsperado, piso.calcularBalanceTotalPendiente().doubleValue(), 0.01);

        piso.registrarPago(java.math.BigDecimal.valueOf(500.0));
        assertEquals(400.0, piso.getPendienteDePago().doubleValue(), 0.01);

        List<Gasto> pendientes = piso.getListaGastosPendientes();
        assertEquals(2, pendientes.size());
    }

    @Test
    public void testRegistroPagoRenta() {
        com.habitly.data.GestorInventario gestor = new com.habitly.data.GestorInventario();
        com.habitly.services.ViviendaService service = new com.habitly.services.ViviendaService(gestor);
        
        Propietario prop = new Propietario("11122233X", "Luis Dueño", "600111222", "luis@mail.com", false, "hash", "salt");
        Inquilino inq = new Inquilino("44455566Y", "Ana Inquilina", "600444555", "ana@mail.com", 95, "hash", "salt");
        gestor.añadirUsuario(prop);
        gestor.añadirUsuario(inq);
        
        Piso piso = new Piso(prop.getDni(), "Calle Mayor 10, 2B", java.math.BigDecimal.valueOf(900.0), 85.0, 3, 1,
                false, false, true, "Excelente", 2, "B");
        piso.setInquilino(inq);
        piso.setEstado(EstadoVivienda.ALQUILADA);
        gestor.añadirVivienda(piso);
        
        gestor.setUsuarioIdentificado(inq);
        
        // Registrar abono de 400€
        boolean result = service.registrarPagoRenta(piso, java.math.BigDecimal.valueOf(400.0));
        assertTrue(result);
        assertEquals(400.0, piso.getTotalPagadoMes().doubleValue(), 0.01);
        assertEquals(500.0, piso.getPendienteDePago().doubleValue(), 0.01);
        
        // Verificar que hay un gasto de pago de renta
        List<Gasto> gastos = piso.getHistorialGastos();
        assertEquals(1, gastos.size());
        Gasto g = gastos.get(0);
        assertEquals("Pago Renta Mensual", g.getConcepto());
        assertEquals(400.0, g.getMonto().doubleValue(), 0.01);
        assertTrue(g.isPagado());
        
        // El beneficio neto del propietario debe seguir siendo de 400.0 (no restando el gasto de renta)
        assertEquals(400.0, gestor.getBeneficioTotal(prop.getDni()).doubleValue(), 0.01);
    }
}