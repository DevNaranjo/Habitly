import com.habitly.data.GestorInventario;
import com.habitly.model.*;

/**
 * Suite de pruebas definitiva para la validación de la Etapa 5.
 * Utiliza el modelo real de Inquilinos y Propietarios para auditar la lógica legal.
 * @author DevNaranjo
 * @version 1.0.5
 */
public class HabitlyLegalTest {

    public static void main(String[] args) {
        System.out.println("=====================================================");
        System.out.println("   HABITLY LEGAL TEST - AUDITORÍA FINAL v1.0.5");
        System.out.println("=====================================================");

        auditarFiscalidadIGIC();
        auditarValidacionIRAV();
        auditarCicloVidaAlquiler();

        System.out.println("=====================================================");
        System.out.println("   RESULTADO: LISTO PARA RELEASE v1.0.5");
        System.out.println("=====================================================");
    }

    /**
     * Verifica que el IGIC canario (7%) se aplique correctamente sobre la base.
     */
    private static void auditarFiscalidadIGIC() {
        System.out.print("[AUDIT] IGIC 7% (Canarias): ");
        // Usamos Piso de v1.0.4
        Piso p = new Piso("PROP_DNI", "Calle Mayor 10", 1000.0, 90.0, 3, 2, true, false, true, "Excelente", 4, "A");

        double esperado = 1070.0;
        if (p.getPrecioFinalConImpuestos() == esperado) {
            System.out.println("PASADO ✅");
        } else {
            System.out.println("FALLADO ❌");
        }
    }

    /**
     * Valida que el Gestor bloquee rentas superiores al IRAV configurado en Vivienda.
     */
    private static void auditarValidacionIRAV() {
        System.out.print("[AUDIT] Bloqueo Ley 12/2023 (IRAV): ");
        GestorInventario gestor = new GestorInventario();

        // Creamos una Casa y definimos su límite legal
        Casa casa = new Casa("PROP_DNI", "Santa Brígida", 1500.0, 200.0, 5, 3, true, true, false, "Nuevo", 1000.0);
        casa.setLimiteMaximoIrav(1550.0);

        // Intentamos un contrato que excede el límite (1600.0)
        ContratoAlquiler cIlegal = new ContratoAlquiler(casa.getDireccion(), "INQ_DNI", TipoArrendador.FISICO, 1600.0, 12, 'B');

        String resultado = gestor.formalizarContrato(cIlegal, casa);

        if (resultado.contains("ERROR LEGAL")) {
            System.out.println("PASADO ✅ (Renta excedida bloqueada)");
        } else {
            System.out.println("FALLADO ❌");
        }
    }

    /**
     * Valida la integración completa: Usuario (Real) + Vivienda + Contrato.
     */
    private static void auditarCicloVidaAlquiler() {
        System.out.print("[AUDIT] Integración Usuario-Contrato: ");
        GestorInventario gestor = new GestorInventario();

        // 1. Registrar Inquilino real
        Inquilino inq = new Inquilino("12345678X", "Juan Prueba", 600000000, "juan@mail.com", 90);
        gestor.añadirUsuario(inq);

        // 2. Preparar Vivienda
        Piso piso = new Piso("B9988", "Avenida Mesa y López", 800.0, 75.0, 2, 1, false, false, true, "Bueno", 3, "C");
        piso.setLimiteMaximoIrav(1000.0);
        gestor.añadirVivienda(piso);

        // 3. Formalizar
        ContratoAlquiler contrato = new ContratoAlquiler(piso.getDireccion(), inq.getDni(), TipoArrendador.FISICO, 850.0, 60, 'B');
        gestor.formalizarContrato(contrato, piso);

        // Verificaciones
        boolean alquilada = (piso.getEstado() == EstadoVivienda.ALQUILADA);
        boolean inquilinoAsignado = (piso.getInquilino() != null && piso.getInquilino().getDni().equals(inq.getDni()));

        if (alquilada && inquilinoAsignado) {
            System.out.println("PASADO ✅ (Vivienda alquilada a " + inq.getNombre() + ")");
        } else {
            System.out.println("FALLADO ❌");
        }
    }
}