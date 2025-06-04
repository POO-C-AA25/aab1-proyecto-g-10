package Model;

public class DetallesFactura {
    private Producto productoAsociado;
    private int unidadesVendidas;
    private double precioIndividual;
    private double valorSubtotal;

    public DetallesFactura() {}

    public DetallesFactura(Producto producto, int cantidad, double precioUnitario) {
        this.productoAsociado = producto;
        this.unidadesVendidas = cantidad;
        this.precioIndividual = precioUnitario;
        calcularSubtotal();
    }

    public void calcularSubtotal() {
        this.valorSubtotal = unidadesVendidas * precioIndividual;
    }  

    public Producto getProducto() {
        return productoAsociado;
    }

    public double getSubtotal() {
        return valorSubtotal;
    }
    
    public int getUnidadesVendidas() {
        return unidadesVendidas;
    }

    public double getPrecioIndividual() {
        return precioIndividual;
    }


    @Override
    public String toString() {
        return String.format(
            "%-8s | %-25s | %4d | %8.2f | %8.2f",
            productoAsociado.getCodigoProducto(), productoAsociado.getDescripcion(),
            unidadesVendidas, precioIndividual, valorSubtotal
        );
    }
}
