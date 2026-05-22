package com.habitly.test;

import com.habitly.data.GestorInventario;
import com.habitly.model.*;
import java.util.List;

/**
 * Suite de auditoría para validar el cumplimiento de la normativa de vivienda.
 * Verifica que las reglas de negocio de la Etapa 5 (IRAV, LAU y Fianzas)
 * se ejecuten correctamente antes del despliegue.
 * @author DevNaranjo
 * @version 1.0.6
 */
public class ValidadorLegalCompliance {

    public static void main(String[] args) {
        ejecutarSuiteAuditoria();
    }

    /**
     * Lanza la batería de pruebas de cumplimiento legal.
     */
    public static void ejecutarSuiteAuditoria() {
        System.out.println("=== INICIANDO AUDITORÍA DE COMPLIANCE (ETAPA 5) ===");

        auditarLimiteRentaIRAV();
        auditarProrrogaLegalSegunArrendador();
        auditarDepositoFianzasICAVI();
        auditarFianzaLegalYGuarantias();
        auditarActualizacionIRAV();
        auditarProrrogaTacita();
        auditarDesistimientoInquilino();

        System.out.println("=== AUDITORÍA FINALIZADA ===");
    }

    /**
     * Prueba que el sistema impida formalizar contratos por encima del índice IRAV.
     */
    private static void auditarLimiteRentaIRAV() {
        System.out.print("[Auditoría IRAV]: ");
        GestorInventario motor = new GestorInventario();

        // Usamos Piso (subclase concreta) ya que Vivienda es abstracta
        Piso v = new Piso("ID_PROPIETARIO", "Av. Marítima, LPGC", 900, 80, 3, 2, true, false, true, "Reformado", 1, "A");
        v.setLimiteMaximoIrav(950.0);

        // Intento de contrato por 1100€ (Excede el límite legal de 950€)
        ContratoAlquiler cIlegal = new ContratoAlquiler(v.getDireccion(), "12345678X", TipoArrendador.FISICO, 1100.0, 12, 'B');

        String respuesta = motor.formalizarContrato(cIlegal, v);

        if (respuesta.startsWith("ERROR LEGAL")) {
            System.out.println("PASADO ✅ (Bloqueo de renta excesiva verificado)");
        } else {
            System.out.println("FALLADO ❌ (Se permitió una renta fuera de índice)");
        }
    }

    /**
     * Valida que la duración mínima legal se ajuste al tipo de arrendador (Art. 9 LAU).
     */
    private static void auditarProrrogaLegalSegunArrendador() {
        System.out.print("[Auditoría Duración]: ");

        // Empresa (JURIDICO) intentando contrato de 5 años (60 meses).
        // Debe fallar porque la ley exige 7 años (84 meses) para personas jurídicas.
        ContratoAlquiler cEmpresa = new ContratoAlquiler("Ref_A", "B12345678", TipoArrendador.JURIDICO, 1200, 60, 'A');

        if (!cEmpresa.cumpleDuracionMinimaLegal()) {
            System.out.println("PASADO ✅ (Identificada insuficiencia de años para Persona Jurídica)");
        } else {
            System.out.println("FALLADO ❌");
        }
    }

    /**
     * Asegura que los contratos sin depósito de fianza confirmado sean detectables.
     */
    private static void auditarDepositoFianzasICAVI() {
        System.out.print("[Auditoría Fianzas]: ");
        GestorInventario motor = new GestorInventario();

        // Usamos Casa (subclase concreta)
        Casa v = new Casa("ID_DUEÑO", "Santa Cruz de Tenerife", 600, 50, 2, 1, false, false, false, "Bueno", 100.0);
        v.setLimiteMaximoIrav(800.0);

        ContratoAlquiler c = new ContratoAlquiler(v.getDireccion(), "99999999R", TipoArrendador.FISICO, 700, 60, 'D');

        motor.añadirVivienda(v);
        motor.formalizarContrato(c, v);

        List<ContratoAlquiler> pendientes = motor.obtenerContratosPendientesFianza();

        // Verificamos si el contrato de la vivienda está en la lista de pendientes de fianza
        if (!pendientes.isEmpty() && !pendientes.get(0).isFianzaDepositada()) {
            System.out.println("PASADO ✅ (Detección de fianza pendiente en ICAVI correcta)");
        } else {
            System.out.println("FALLADO ❌");
        }
    }

    /**
     * Asegura que el sistema valide correctamente la fianza legal y límites de garantías.
     */
    private static void auditarFianzaLegalYGuarantias() {
        System.out.print("[Auditoría Fianza Legal y Garantías]: ");
        GestorInventario motor = new GestorInventario();

        Piso v = new Piso("ID_PROP", "Mesa y Lopez, LPGC", 800, 70, 2, 1, false, false, true, "Reformado", 2, "B");
        v.setLimiteMaximoIrav(900.0);
        motor.añadirVivienda(v);

        // 1. Fianza errónea (no es exactamente 1 mes de renta)
        ContratoAlquiler cFianzaErronea = new ContratoAlquiler(v.getDireccion(), "12345678X", TipoArrendador.FISICO, 800.0, 60, 'E', 700.0, 0.0);
        String res1 = motor.formalizarContrato(cFianzaErronea, v);

        // 2. Garantías excesivas (> 2 meses)
        ContratoAlquiler cGarantiaErronea = new ContratoAlquiler(v.getDireccion(), "12345678X", TipoArrendador.FISICO, 800.0, 60, 'F', 800.0, 1700.0);
        String res2 = motor.formalizarContrato(cGarantiaErronea, v);

        // 3. Contrato correcto
        ContratoAlquiler cCorrecto = new ContratoAlquiler(v.getDireccion(), "12345678X", TipoArrendador.FISICO, 800.0, 60, 'G', 800.0, 1600.0);
        String res3 = motor.formalizarContrato(cCorrecto, v);

        if (res1.startsWith("ERROR LEGAL") && res2.startsWith("ERROR LEGAL") && res3.startsWith("ÉXITO")) {
            System.out.println("PASADO ✅ (Fianza y límites de garantía verificados exitosamente)");
        } else {
            System.out.println("FALLADO ❌ (Fallo en la validación de fianza/garantías: res1=" + res1 + ", res2=" + res2 + ", res3=" + res3 + ")");
        }
    }

    /**
     * Valida que la actualización anual de renta (IRAV) solo se aplique en fecha de aniversario.
     */
    private static void auditarActualizacionIRAV() {
        System.out.print("[Auditoría Actualización IRAV]: ");
        ContratoAlquiler c = new ContratoAlquiler("Calle Triana", "12345678X", TipoArrendador.FISICO, 1000.0, 60, 'A');

        // 1. Sin aniversario y sin forzar -> Debe fallar
        boolean sinAniv = c.aplicarActualizacionIRAV(2.5, false);

        // 2. Sin aniversario pero forzando -> Debe actualizar
        boolean forzado = c.aplicarActualizacionIRAV(2.5, true);
        double rentaForzada = c.getRentaMensual();

        // 3. Simular aniversario (1 año en el pasado)
        c.setRentaMensual(1000.0);
        c.setFechaFirma(java.time.LocalDate.now().minusYears(1));
        boolean aniversario = c.aplicarActualizacionIRAV(3.0, false);
        double rentaAniversario = c.getRentaMensual();

        if (!sinAniv && forzado && rentaForzada == 1025.0 && aniversario && rentaAniversario == 1030.0) {
            System.out.println("PASADO ✅ (Control de aniversario IRAV verificado)");
        } else {
            System.out.println("FALLADO ❌ (Error en actualización IRAV: sinAniv=" + sinAniv + ", rentaForzada=" + rentaForzada + ", aniversario=" + aniversario + ", rentaAniversario=" + rentaAniversario + ")");
        }
    }

    /**
     * Valida el comportamiento de la prórroga tácita hasta el límite legal de 3 años.
     */
    private static void auditarProrrogaTacita() {
        System.out.print("[Auditoría Prórroga Tácita]: ");
        ContratoAlquiler c = new ContratoAlquiler("Calle Triana", "12345678X", TipoArrendador.FISICO, 1000.0, 60, 'A');
        
        java.time.LocalDate vtoOriginal = c.getFechaVencimiento();

        // Aplicamos prórrogas tácitas anuales
        boolean p1 = c.activarProrrogaTacita(); // 1 año consumido
        boolean p2 = c.activarProrrogaTacita(); // 2 años consumidos
        boolean p3 = c.activarProrrogaTacita(); // 3 años consumidos
        boolean p4 = c.activarProrrogaTacita(); // Supera el límite de 3 años

        java.time.LocalDate vtoFinal = c.getFechaVencimiento();
        boolean vencimientoExtendioTresAnios = vtoFinal.isEqual(vtoOriginal.plusYears(3));

        if (p1 && p2 && p3 && !p4 && c.getEstado() == EstadoContrato.FINALIZADO && vencimientoExtendioTresAnios) {
            System.out.println("PASADO ✅ (Prórroga tácita limitada a 3 años verificada)");
        } else {
            System.out.println("FALLADO ❌ (Error en prórroga tácita: p1=" + p1 + ", p2=" + p2 + ", p3=" + p3 + ", p4=" + p4 + ", estado=" + c.getEstado() + ")");
        }
    }

    /**
     * Valida la restricción legal del desistimiento de contrato antes de 6 meses.
     */
    private static void auditarDesistimientoInquilino() {
        System.out.print("[Auditoría Desistimiento]: ");
        ContratoAlquiler c = new ContratoAlquiler("Calle Triana", "12345678X", TipoArrendador.FISICO, 1000.0, 60, 'A');

        // Hoy firmado -> No permitido desistir
        boolean dHoy = c.estaEnPeriodoDesistimiento();

        // 6 meses en el pasado -> Permitido
        c.setFechaFirma(java.time.LocalDate.now().minusMonths(6));
        boolean dPasado = c.estaEnPeriodoDesistimiento();

        if (!dHoy && dPasado) {
            System.out.println("PASADO ✅ (Periodo de exclusión de 6 meses verificado)");
        } else {
            System.out.println("FALLADO ❌ (Error en desistimiento: dHoy=" + dHoy + ", dPasado=" + dPasado + ")");
        }
    }
}