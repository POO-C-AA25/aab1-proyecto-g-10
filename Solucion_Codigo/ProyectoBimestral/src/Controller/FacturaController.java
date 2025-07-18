package Controller;

import Model.*;

public class FacturaController {
    private Factura factura;
    private Deducibles deducibles;

    public FacturaController() {
        this.factura = new Factura();
        this.deducibles = new Deducibles();
    }

    public void asignarCliente(Cliente cliente) {
        factura.asignarCliente(cliente);
    }

    public void añadirProductoAFactura(Producto producto, int cantidad) {
        double precio = producto.getPrecioBase();

        // Aplicar descuento por condiciones (vencimiento cercano o stock alto)
        if (producto.getUnidadesDisponibles() > 20 || 
            producto.getVencimiento().isBefore(java.time.LocalDate.now().plusDays(10))) {
            if (producto instanceof ProductoImportado) {
                precio *= 0.90;
            } else if (producto instanceof ProductoLocal) {
                precio *= 0.85;
            }
        }

        DetallesFactura detalle = new DetallesFactura(producto, cantidad, precio);
        factura.añadirDetalle(detalle);
        producto.reducirInventario(cantidad);
    }

    public void calcularTotales(double porcentajeIVA) {
        double subtotal = 0.0;
        for (DetallesFactura detalle : factura.getDetalles()) {
            subtotal += detalle.getSubtotal();
        }
        factura.setMontoBase(subtotal);

        double impuesto = subtotal * porcentajeIVA;
        factura.setImpuesto(impuesto);

        double totalDeducciones = 0.0;

        if (factura.getDatosCliente() != null) {
            String tipo = factura.getDatosCliente().getTipoCliente();
            for (DetallesFactura df : factura.getDetalles()) {
                totalDeducciones += deducibles.calcularMontoDeducible(
                    df.getProducto().getCategoria(), tipo, df.getSubtotal()
                );
            }
        }

        double totalFinal = subtotal + impuesto - totalDeducciones;
        factura.setMontoFinal(totalFinal);
    }

    public Factura getFactura() {
        return factura;
    }
}
