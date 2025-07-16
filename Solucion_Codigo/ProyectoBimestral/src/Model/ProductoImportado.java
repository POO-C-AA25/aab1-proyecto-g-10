package Model;

/**
 * Producto importado que tiene recargo de arancel y no aplica descuentos.
 * @author Usuario
 */
public class ProductoImportado extends Producto{
    // Porcentaje de arancel 
    private double porcentajeArancel = 0.15;

    public ProductoImportado(){
        // Constructor vacío
    }
    
    public ProductoImportado(String descripcion, String categoria, double precioBase, int unidadesDisponibles) {
        super(descripcion, categoria, precioBase, unidadesDisponibles);
    }

    public ProductoImportado(String descripcion, String categoria, double precioBase, int unidadesDisponibles, double porcentajeArancel) {
        super(descripcion, categoria, precioBase, unidadesDisponibles);
        this.porcentajeArancel = porcentajeArancel;
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
