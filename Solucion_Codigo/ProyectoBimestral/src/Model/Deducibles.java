package Model;

/**
 * Clase encargada de calcular los porcentajes deducibles del impuesto
 * en función de la categoría del producto y del tipo de cliente.
 * @autor Usuario
 */
public class Deducibles {

    /**
     * Retorna el porcentaje de deducción según la categoría del producto
     * y el tipo de cliente (NATURAL o EMPRESA).
     */
    public double obtenerPorcentaje(String categoria, String tipoCliente) {
        String cat = categoria.trim().toUpperCase();
        String tipo = tipoCliente.trim().toUpperCase();

        switch (cat) {
            case "ALIMENTACION":
                return tipo.equals("EMPRESA") ? 0.10 : 0.15;
            case "EDUCACION":
                return tipo.equals("EMPRESA") ? 0.12 : 0.20;
            case "VIVIENDA":
                return tipo.equals("EMPRESA") ? 0.06 : 0.10;
            case "VESTIMENTA":
                return tipo.equals("EMPRESA") ? 0.09 : 0.18;
            case "SALUD":
                return tipo.equals("EMPRESA") ? 0.15 : 0.22;
            default:
                return 0.0;  // Categoría desconocida
        }
    }

    public double calcularMontoDeducible(String categoria, String tipoCliente, double valor) {
        return obtenerPorcentaje(categoria, tipoCliente) * valor;
    }
}

