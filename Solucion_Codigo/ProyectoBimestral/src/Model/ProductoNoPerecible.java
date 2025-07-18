package Model;

/**
 * Producto que no tiene fecha de vencimiento y aplica descuento por stock alto.
 * @author Usuario
 */
public class ProductoNoPerecible extends Producto {
    
    public ProductoNoPerecible() {
        // Constructor vacío
    }
    
    public ProductoNoPerecible(String descripcion, String categoria, double precioBase, int unidadesDisponibles, String etiqueta) {
        super(descripcion, categoria, precioBase, unidadesDisponibles, etiqueta);
    }
    
    @Override
    public String obtenerInformacionExtra() {
        return "No perecible";
    }

    @Override
    public double calcularPrecioFinal() {
        if (getUnidadesDisponibles() > 50) {
            return getPrecioBase() * 0.9;  // 10% descuento por stock alto
        }
        return getPrecioBase();
    }
}