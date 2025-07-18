package Model;

/**
 * Producto importado que tiene recargo de arancel y no aplica descuentos.
 * @author Mateo Gonzáles y Mateo Rivera
 */
public class ProductoImportado extends Producto{
    private double porcentajeArancel = 0.15;
    
    public ProductoImportado(){
        // Constructor vacío
    }
    
    public ProductoImportado(String descripcion, String categoria, double precioBase, int unidadesDisponibles, String etiqueta) {
        super(descripcion, categoria, precioBase, unidadesDisponibles, etiqueta);
    }

    public double getPorcentajeArancel() {
        return porcentajeArancel;
    }

    public void setPorcentajeArancel(double porcentajeArancel) {
        this.porcentajeArancel = porcentajeArancel;
    }
    
    @Override
    public String obtenerInformacionExtra() {
        return String.format("Importado - Recargo %.2f%% arancel", porcentajeArancel * 100);
    }

    @Override
    public double calcularPrecioFinal() {
        return getPrecioBase() * (1 + porcentajeArancel);
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nPorcentaje Arancel: " + porcentajeArancel * 100 + "%\n";
    }
}
