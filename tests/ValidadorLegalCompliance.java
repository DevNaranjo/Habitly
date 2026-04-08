package com.habitly.test;

import com.habitly.data.GestorInventario;
import com.habitly.model.*;
import java.util.List;

/**
 * Suite de auditoría para validar el cumplimiento de la normativa de vivienda.
 * Verifica que las reglas de negocio de la Etapa 5 (IRAV, LAU y Fianzas)
 * se ejecuten correctamente antes del despliegue.
 * @author DevNaranjo
 * @version 1.0.5
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

        System.out.println("=== AUDITORÍA FINALIZADA ===");
    }

    /**
     * Prueba que el sistema impida formalizar contratos por encima del índice IRAV.
     */
    private static void auditarLimiteRentaIRAV() {
        System.out.print("[Auditoría IRAV]: ");
        GestorInventario motor = new GestorInventario();

        // Usamos Piso (subclase concreta) ya que Vivienda es abstracta
        // Parámetros: idProp, dir, precio, m2, hab, baños, garaje, piscina, amueblado, cons, planta, puerta
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
        // Parámetros: idProp, dir, precio, m2, hab, baños, garaje, piscina, amueblado, cons, metrosParcela
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
}