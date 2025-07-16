package View;

import Model.EstadisticaVentas;
import Model.EstadisticaVentas.RegistroProducto;
import Model.EstadisticaVentas.RegistroCategoria;

import java.util.ArrayList;

public class ReporteEstadisticasView {

    public static void mostrarResumen(EstadisticaVentas estadistica) {
        ArrayList<RegistroProducto> productos = estadistica.getVentasPorProductoOrdenadas();
        ArrayList<RegistroCategoria> categorias = estadistica.getVentasPorCategoriaOrdenadas();

        if (productos.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        RegistroProducto masVendido = productos.get(0);
        RegistroProducto menosVendido = productos.get(productos.size() - 1);
        RegistroCategoria categoriaTop = categorias.get(0);

        System.out.println("=== PRODUCTO MÁS VENDIDO ===");
        System.out.printf("%s (%s): %d unidades%n", masVendido.nombreProducto, masVendido.codigoProducto, masVendido.unidadesVendidas);

        System.out.println("=== PRODUCTO MENOS VENDIDO ===");
        System.out.printf("%s (%s): %d unidades%n", menosVendido.nombreProducto, menosVendido.codigoProducto, menosVendido.unidadesVendidas);

        System.out.println("=== CATEGORÍA MÁS VENDIDA ===");
        System.out.printf("%s: %d unidades%n", categoriaTop.nombreCategoria, categoriaTop.unidadesVendidas);
    }
}