package Model;

/**
 *
 * @author Usuario
 */
public class ConsumidorFinal extends Cliente{
    
    public ConsumidorFinal() {
        // Datos genéricos
        super("9999999999", "Consumidor Final", "N/A", "N/A", "No especificado");
    }

    @Override
    public String getTipoCliente() {
        return "CONSUMIDOR_FINAL";
    }

    @Override
    public String toString() {
        return "Consumidor Final";
    }
}
