package Model;

/**
 * Producto local que aplica descuento promocional especial.
 * @author Usuario
 */
public class ProductoLocal extends Producto{
    // Porcentaje de descuento promocional
    private double porcentajeDescuento = 0.05;

    public ProductoLocal() {
        // Constructor vacío
    }
    
    public ProductoLocal(String descripcion, String categoria, double precioBase, int unidadesDisponibles) {
        super(descripcion, categoria, precioBase, unidadesDisponibles);
    }

    public ProductoLocal(String descripcion, String categoria, double precioBase, int unidadesDisponibles, double porcentajeDescuento) {
        super(descripcion, categoria, precioBase, unidadesDisponibles);
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }
    
    @Override
    public String obtenerInformacionExtra() {
        return String.format("Producto local con %.2f%% descuento", porcentajeDescuento * 100);
    }

    @Override
    public double calcularPrecioFinal() {
        return getPrecioBase() * (1 - porcentajeDescuento);
    }

    @Override
    public String toString() {
        return super.toString() + "\nPorcentaje Descuento: " + porcentajeDescuento * 100 + "%\n";
    }
}
