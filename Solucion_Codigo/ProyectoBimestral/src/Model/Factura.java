package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class Factura implements Serializable {
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

    /**
     * Calcula el subtotal sumando los subtotales de cada detalle.
     */
    public void calcularMontoBase() {
        montoBase = 0.0;
        for (DetallesFactura df : detalles) {
            montoBase += df.getSubtotal();
        }
    }

    /**
     * Calcula el impuesto (IVA) según el porcentaje indicado.
     * @param porcentajeIVA debe estar en decimal (ej: 0.12 para 12%)
     */
    public void calcularImpuesto(double porcentajeIVA) {
        impuesto = montoBase * porcentajeIVA;
    }

    /**
     * Calcula el monto final restando las deducciones según categoría y tipo de cliente.
     * Usa la clase Deducibles para obtener los porcentajes correctos.
     * 
     * @param deducibles instancia para calcular deducciones
     */
    public void calcularMontoFinal(Deducibles deducibles) {
        double totalDeducciones = 0.0;

        if (datosCliente == null) {
            // Si no hay cliente asignado, no aplicar deducciones
            montoFinal = montoBase + impuesto;
            return;
        }

        String tipoCliente = datosCliente.getTipoCliente();

        for (DetallesFactura df : detalles) {
            double deducible = deducibles.calcularMontoDeducible(
                df.getProducto().getCategoria(),
                tipoCliente,
                df.getSubtotal()
            );
            totalDeducciones += deducible;
        }
        montoFinal = montoBase + impuesto - totalDeducciones;
    }

    // Getters
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

    /**
     * Representación formateada de la factura para imprimir.
     */
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
        if (datosCliente != null) {
            sb.append("| Cliente: ").append(String.format("%-60s", datosCliente.getNombre())).append("|\n");
            sb.append("| CI/Número: ").append(String.format("%-58s", datosCliente.getId())).append("|\n");
            sb.append("| Correo:   ").append(String.format("%-58s", datosCliente.getEmail())).append("|\n");
            sb.append("| Teléfono: ").append(String.format("%-58s", datosCliente.getCelular())).append("|\n");
            sb.append("| Dirección:").append(String.format("%-58s", datosCliente.getUbicacion())).append("|\n");
        } else {
            sb.append("| Cliente: ").append(String.format("%-60s", "No asignado")).append("|\n");
        }
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