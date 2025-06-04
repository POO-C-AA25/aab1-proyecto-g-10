package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class Factura {
    private String codigoFactura;
    private LocalDate fecha;
    private Cliente datosCliente;
    private ArrayList<DetallesFactura> detalles;
    private double montoBase;
    private double impuesto;
    private double montoFinal;

    public Factura() {
        this.codigoFactura = UUID.randomUUID().toString().substring(0, 8);
        this.fecha = LocalDate.now();
        this.detalles = new ArrayList<>();
        this.montoBase = 0.0;
        this.impuesto = 0.0;
        this.montoFinal = 0.0;
    }

    public void asignarCliente(Cliente c) {
        this.datosCliente = c;
    }
    
    public ArrayList<DetallesFactura> getDetalles() {
        return detalles;
    }

    public void añadirDetalle(DetallesFactura d) {
        detalles.add(d);
    }

    public void calcularMontoBase() {
        montoBase = 0.0;
        for (DetallesFactura df : detalles) {
            montoBase += df.getSubtotal();
        }
    }

    public void calcularImpuesto(double porcentajeIVA) {
        impuesto = montoBase * porcentajeIVA;
    }

    public void calcularMontoFinal(Deducibles deducible) {
        double totalDeducciones = 0.0;
        for (DetallesFactura df : detalles) {
            totalDeducciones += deducible.calcularMontoDeducible(
                df.getProducto().getCategoria(),
                df.getSubtotal()
            );
        }
        montoFinal = montoBase + impuesto - totalDeducciones;
    }
    
    public String getCodigoFactura() {
        return codigoFactura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getDatosCliente() {
        return datosCliente;
    }

    public double getMontoBase() {
        return montoBase;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public double getMontoFinal() {
        return montoFinal;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("+----------------------------------------------------------------------+\n");
        sb.append("|                       SUPERMERCADO SUPERMAXI                         |\n");
        sb.append("|                      Loja - \"El placer de comprar\"                   |\n");
        sb.append("+----------------------------------------------------------------------+\n");
        sb.append("| FACTURA N°: ").append(String.format("%-55s", codigoFactura)).append("|\n");
        sb.append("| Fecha Emisión: ").append(String.format("%-50s", fecha)).append("|\n");
        sb.append("+----------------------------------------------------------------------+\n");
        sb.append("| Cliente: ").append(String.format("%-60s", datosCliente.getNombre())).append("|\n");
        sb.append("| CI/Número: ").append(String.format("%-58s", datosCliente.getId())).append("|\n");
        sb.append("| Correo:   ").append(String.format("%-58s", datosCliente.getCorreo())).append("|\n");
        sb.append("| Teléfono: ").append(String.format("%-58s", datosCliente.getTelefono())).append("|\n");
        sb.append("| Dirección:").append(String.format("%-58s", datosCliente.getDireccion())).append("|\n");
        sb.append("+----------------------------------------------------------------------+\n");
        sb.append("| Detalles de la compra:                                                |\n");
        sb.append("+------------+------------------------------+------+--------+--------+\n");
        sb.append("| ProductoID | Nombre del Producto           | Cant | Precio | Total  |\n");
        sb.append("+------------+------------------------------+------+--------+--------+\n");

        for (DetallesFactura df : detalles) {
            sb.append("| ")
              .append(String.format("%-10s | ", df.getProducto().getCodigoProducto()))
              .append(String.format("%-28s | ", df.getProducto().getDescripcion()))
              .append(String.format("%4d | ", df.getUnidadesVendidas()))
              .append(String.format("%6.2f | ", df.getPrecioIndividual()))
              .append(String.format("%6.2f |\n", df.getSubtotal()));
        }

        sb.append("+----------------------------------------------------------------------+\n");
        sb.append(String.format("| Subtotal: %-58.2f |\n", montoBase));
        sb.append(String.format("| IVA:      %-58.2f |\n", impuesto));
        sb.append(String.format("| Total:    %-58.2f |\n", montoFinal));
        sb.append("+----------------------------------------------------------------------+\n");
        return sb.toString();
    }
}

