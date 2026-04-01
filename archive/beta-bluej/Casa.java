/**
 * Representa una vivienda de tipo Casa o Unifamiliar.
 * Extiende la funcionalidad de Vivienda añadiendo información sobre el terreno.
 * * @author DevNaranjo
 * @version BETA
 * @since 29-03-26
 */
public class Casa extends Vivienda 
{
    // Atributo específico: superficie de terreno privado
    private double metrosParcela;

    /**
     * Constructor para la clase Casa.
     * Delega la configuración de dirección y precio a la superclase.
     * * @param direccion Ubicación de la casa.
     * @param precioBase Mensualidad neta acordada.
     * @param metrosParcela Superficie total del terreno en m².
     */
    public Casa(String direccion, double precioBase, double metrosParcela) 
    {
        super(direccion, precioBase);
        this.metrosParcela = metrosParcela;
    }

    // --- Getters ---

    /**
     * Obtiene la superficie de la parcela.
     * * @return double con los metros cuadrados del terreno.
     */
    public double getMetrosParcela() 
    {
        return metrosParcela;
    }
    
    // --- Setters ---

    /**
     * Permite modificar la extensión de la parcela registrada.
     * * @param nuevosMetrosParcela Valor decimal con la nueva superficie.
     */
    public void setMetrosParcela(double nuevosMetrosParcela) 
    {
        this.metrosParcela = nuevosMetrosParcela;
    }
}