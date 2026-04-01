package com.habitly.model;

/**
 * La clase Vivienda representa un inmueble genérico en alquiler dentro del sistema.
 * Centraliza la gestión de la ubicación, el precio base y la normativa fiscal (IGIC).
 * Sirve como superclase para tipos de vivienda más específicos como Piso o Casa.
 * * @author DevNaranjo
 * @version BETA
 * @since 29-03-26
 */
public class Vivienda
{
    /**
     * Tasa del Impuesto General Indirecto Canario aplicable (7.0%).
     */
    private static final double IGIC = 7.0;

    //Atributos específicos para la clase
    private String direccion;
    private double precioBase;

    //Atributos de control
    private double mensualidadTotal;
    private double totalPagadoMes;

    /**
     * Constructor principal para la clase Vivienda.
     * * @param direccion La ubicación física completa del inmueble.
     * @param precioBase El importe mensual del alquiler antes de impuestos.
     */
    public Vivienda(String direccion, double precioBase)
    {
        this.direccion = direccion;
        this.precioBase = precioBase;

        this.mensualidadTotal = getPrecioConImpuestos();
        this.totalPagadoMes = 0;
    }

    // --- Getters ---

    /**
     * Obtiene la dirección registrada de la vivienda.
     * * @return String con la dirección del inmueble.
     */
    public String getDireccion()
    {
        return direccion;
    }

    /**
     * Obtiene el precio base mensual actual (neto).
     * * @return double con el precio base sin impuestos.
     */
    public double getPrecioBase()
    {
        return precioBase;
    }

    // --- Setters ---

    /**
     * Actualiza la dirección física de la propiedad.
     * * @param nuevaDireccion La nueva dirección a asignar.
     */
    public void setDireccion(String nuevaDireccion)
    {
        this.direccion = nuevaDireccion;
    }

    /**
     * Modifica el precio base del alquiler.
     * * @param nuevoPrecioBase El nuevo importe neto mensual.
     */
    public void setPrecioBase(double nuevoPrecioBase)
    {
        this.precioBase = nuevoPrecioBase;
        this.mensualidadTotal = getPrecioConImpuestos();
    }

    // --- Métodos lógica financiera ---

    /**
     * Calcula el importe total de la mensualidad aplicando el gravamen
     * correspondiente al IGIC (7.0%).
     * * @return double con el precio final (precioBase + impuestos).
     */
    public double getPrecioConImpuestos()
    {
        return precioBase * (1 + IGIC / 100.0);
    }

    // --- Métodos gestión financiero ---

    /**
     * Registra un ingreso de dinero para la mensualidad actual.
     * Incrementa el acumulador de pagos realizados por el inquilino.
     * * @param cuota La cantidad de dinero (en euros) que se desea abonar.
     */
    public void registrarPago(double cuota)
    {
        totalPagadoMes += cuota;
    }

    /**
     * Calcula la deuda restante del mes actual.
     * * @return El importe pendiente (double) para cubrir la mensualidad total.
     */
    public double getPendienteDePago()
    {
        return mensualidadTotal - totalPagadoMes;
    }

    /**
     * Determina si el inquilino ha cumplido con su obligación financiera.
     * Valida tanto el pago exacto como los posibles sobrepagos.
     * * @return true si el total pagado es mayor o igual a la mensualidad;
     * false si aún existe saldo pendiente.
     */
    public boolean isPagadoCompleto()
    {
        return totalPagadoMes >= mensualidadTotal;
    }

    /**
     * Calcula la proyección de la cantidad a ingresar en el futuro (máximo 1 año)
     *
     * @param meses El número de meses que se desea proyectar la cantidad.
     * @return Devuelve la cantidad total de euros a ingresar en los meses pasados por
     *         parámetro.
     *         En caso de que los meses sean incorrectos, devolverá 0.0.
     */
    public double getProyeccionIngresos(int meses)
    {
        if (meses < 1 || meses > 12)
        {
            return 0.0;
        }

        return mensualidadTotal * meses;
    }

    /**
     * Aplica un incremento porcentual al precio base de la vivienda (IPC).
     * * Este método actualiza el precioBase y, consecuentemente, recalcula
     * la mensualidadTotal mediante el uso del setter.
     * * @param porcentaje El valor porcentual a subir.
     */
    public void aplicarSubidaAnual(double porcentaje)
    {
        double nuevoPrecio = precioBase + (precioBase * porcentaje / 100);
        setPrecioBase(nuevoPrecio);
    }
}