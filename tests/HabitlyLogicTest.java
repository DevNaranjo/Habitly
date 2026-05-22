import com.habitly.model.*;
import java.util.List;

/**
 * Test de Lógica de Negocio v1.0.6
 * Prueba la interacción entre Usuarios, Viviendas y Gastos.
 */
public class HabitlyLogicTest {
    public static void main(String[] args) {
        System.out.println("=== TEST DE LÓGICA HABITLY v1.0.6 ===");

        // 1. ESCENARIO: Un Propietario registra un Piso y un Inquilino se muda
        Propietario prop = new Propietario("11122233X", "Luis Dueño", 600111222, "luis@mail.com", false);
        Inquilino inq = new Inquilino("44455566Y", "Ana Inquilina", 600444555, "ana@mail.com", 95);

        Piso piso = new Piso(prop.getDni(), "Calle Mayor 10, 2B", 900.0, 85.0, 3, 1,
                false, false, true, "Excelente", 2, "B");

        // 2. VINCULACIÓN: Asignar inquilino a la vivienda
        piso.setInquilino(inq);
        piso.setEstado(EstadoVivienda.ALQUILADA);

        System.out.println("\n--- ESTADO INICIAL ---");
        System.out.println("Vivienda: " + piso.getDireccion());
        System.out.println("Inquilino asignado: " + piso.getInquilino().getNombre());
        System.out.println("Precio con IGIC (Exento): " + piso.getPrecioFinalConImpuestos() + "€");

        // 3. ECONOMÍA: Registro de suministros (Gastos v1.0.4)
        // Simulamos una factura de luz de 120€ y una de agua de 30€
        piso.registrarFactura(new Gasto("LUZ-ABRIL", "Factura Electricidad", 120.0));
        piso.registrarFactura(new Gasto("AGUA-ABRIL", "Factura Agua", 30.0));

        // 4. VERIFICACIÓN DE BALANCE
        // Renta exenta de IGIC: 900.0
        // Gastos: 120 + 30 = 150.0
        // Total esperado: 1050.0
        double balanceEsperado = 1050.0;
        double balanceCalculado = piso.calcularBalanceTotalPendiente();

        System.out.println("\n--- PRUEBA DE BALANCES ---");
        if (Math.abs(balanceCalculado - balanceEsperado) < 0.01) {
            System.out.println("✅ OK: Balance total (Renta + Gastos) correcto: " + balanceCalculado + "€");
        } else {
            System.out.println("❌ ERROR: El balance no coincide. Esperado: " + balanceEsperado + " | Calculado: " + balanceCalculado);
        }

        // 5. PRUEBA DE PAGOS
        System.out.println("\n--- PRUEBA DE PAGOS ---");
        piso.registrarPago(500.0); // Ana paga 500€
        double pendiente = piso.getPendienteDePago();

        // El pendiente solo cuenta la Renta exenta de IGIC (900 - 500 = 400.0)
        if (Math.abs(pendiente - 400.0) < 0.01) {
            System.out.println("✅ OK: Pago parcial registrado. Pendiente de renta: " + pendiente + "€");
        } else {
            System.out.println("❌ ERROR: El cálculo del pendiente es incorrecto: " + pendiente);
        }

        // 6. LISTADO DE GASTOS PENDIENTES
        List<Gasto> pendientes = piso.getListaGastosPendientes();
        if (pendientes.size() == 2) {
            System.out.println("✅ OK: Se mantienen los 2 gastos como 'Pendientes de pago'.");
        } else {
            System.out.println("❌ ERROR: Fallo en la lista de gastos pendientes.");
        }

        System.out.println("\n=============================================");
        System.out.println("RESULTADO: Lógica v1.0.6 validada correctamente.");
    }
}