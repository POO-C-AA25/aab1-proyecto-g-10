package View;

import Model.DetallesFactura;
import Model.Factura;

public class FacturaView {
    public static void mostrarFactura(Factura factura) {
        System.out.println("+----------------------------------------------------------------------+");
        System.out.println("|                       SUPERMERCADO SUPERMAXI                         |");
        System.out.println("|                      Loja - \"El placer de comprar\"                   |");
        System.out.println("+----------------------------------------------------------------------+");
        System.out.printf("| FACTURA N°: %-60s|\n", factura.getCodigoFactura());
        System.out.printf("| Fecha Emisión: %-55s|\n", factura.getFecha());
        System.out.println("+----------------------------------------------------------------------+");

        if (factura.getDatosCliente() != null) {
            System.out.printf("| Cliente: %-60s|\n", factura.getDatosCliente().getNombre());
            System.out.printf("| CI/Número: %-58s|\n", factura.getDatosCliente().getId());
            System.out.printf("| Correo:   %-58s|\n", factura.getDatosCliente().getEmail());
            System.out.printf("| Teléfono: %-58s|\n", factura.getDatosCliente().getCelular());
            System.out.printf("| Dirección:%-58s|\n", factura.getDatosCliente().getUbicacion());
        } else {
            System.out.printf("| Cliente: %-60s|\n", "No asignado");
        }

        System.out.println("+----------------------------------------------------------------------+");
        System.out.println("| Detalles de la compra:                                               |");
        System.out.println("+------------+------------------------------+------+--------+--------+");
        System.out.println("| ProductoID | Nombre del Producto           | Cant | Precio | Total  |");
        System.out.println("+------------+------------------------------+------+--------+--------+");

        for (DetallesFactura df : factura.getDetalles()) {
            System.out.printf("| %-10s | %-28s | %4d | %6.2f | %6.2f |\n",
                    df.getProducto().getCodigoProducto(),
                    df.getProducto().getDescripcion(),
                    df.getUnidadesVendidas(),
                    df.getPrecioIndividual(),
                    df.getSubtotal());
        }

        System.out.println("+----------------------------------------------------------------------+");
        System.out.printf("| Subtotal: %-58.2f |\n", factura.getMontoBase());
        System.out.printf("| IVA:      %-58.2f |\n", factura.getImpuesto());
        System.out.printf("| Total:    %-58.2f |\n", factura.getMontoFinal());
        System.out.println("+----------------------------------------------------------------------+");
    }
}