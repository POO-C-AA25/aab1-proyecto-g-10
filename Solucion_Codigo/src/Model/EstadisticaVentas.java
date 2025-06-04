package Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;

/**
 * Clase encargada de registrar, analizar y exportar estadísticas
 * de ventas por producto y por categoría.
 */
public class EstadisticaVentas {

    public static class RegistroProducto {
        public String codigoProducto;
        public String nombreProducto;
        public int unidadesVendidas;
        public double totalGenerado;

        public RegistroProducto(String codigo, String nombre) {
            this.codigoProducto = codigo;
            this.nombreProducto = nombre;
            this.unidadesVendidas = 0;
            this.totalGenerado = 0.0;
        }
    }

    public static class RegistroCategoria {
        public String nombreCategoria;
        public int unidadesVendidas;
        public double totalGenerado;

        public RegistroCategoria(String categoria) {
            this.nombreCategoria = categoria;
            this.unidadesVendidas = 0;
            this.totalGenerado = 0.0;
        }
    }

    // Listas que acumulan las ventas totales agrupadas
    private ArrayList<RegistroProducto> ventasPorProducto;
    private ArrayList<RegistroCategoria> ventasPorCategoria;

    public EstadisticaVentas() {
        ventasPorProducto = new ArrayList<>();
        ventasPorCategoria = new ArrayList<>();
    }

    public void agregarDetalleFactura(DetallesFactura detalle) {
        String codigo = detalle.getProducto().getCodigoProducto();
        String nombre = detalle.getProducto().getDescripcion();
        String categoria = detalle.getProducto().getCategoria();
        int cantidad = detalle.getUnidadesVendidas();
        double subtotal = detalle.getSubtotal();

        // Actualizar ventas por producto
        RegistroProducto prod = buscarProductoRegistrado(codigo);
        if (prod == null) {
            prod = new RegistroProducto(codigo, nombre);
            ventasPorProducto.add(prod);
        }
        prod.unidadesVendidas += cantidad;
        prod.totalGenerado += subtotal;

        // Actualizar ventas por categoría
        RegistroCategoria cat = buscarCategoriaRegistrada(categoria);
        if (cat == null) {
            cat = new RegistroCategoria(categoria);
            ventasPorCategoria.add(cat);
        }
        cat.unidadesVendidas += cantidad;
        cat.totalGenerado += subtotal;
    }

    /** Busca si el producto ya tiene un registro previo de ventas */
    private RegistroProducto buscarProductoRegistrado(String codigo) {
        for (RegistroProducto rp : ventasPorProducto) {
            if (rp.codigoProducto.equals(codigo)) {
                return rp;
            }
        }
        return null;
    }

    /** Busca si la categoría ya tiene un registro previo de ventas */
    private RegistroCategoria buscarCategoriaRegistrada(String nombreCategoria) {
        for (RegistroCategoria rc : ventasPorCategoria) {
            if (rc.nombreCategoria.equalsIgnoreCase(nombreCategoria)) {
                return rc;
            }
        }
        return null;
    }

    /**
     * Guarda las estadísticas en un archivo CSV llamado "datosestadisticas.csv".
     * Incluye dos secciones: por producto y por categoría.
     */
    public void persistirEstadisticasCSV() {
        try (Formatter f = new Formatter("datosestadisticas.csv")) {
            f.format("Ventas por Producto%n");
            f.format("codigoProducto;nombreProducto;unidadesVendidas;totalGenerado%n");
            for (RegistroProducto rp : ventasPorProducto) {
                f.format("%s;%s;%d;%.2f%n", rp.codigoProducto, rp.nombreProducto, rp.unidadesVendidas, rp.totalGenerado);
            }

            // Sección: Ventas por Categoría
            f.format("%nVentas por Categoría%n");
            f.format("nombreCategoria;unidadesVendidas;totalGenerado%n");
            for (RegistroCategoria rc : ventasPorCategoria) {
                f.format("%s;%d;%.2f%n", rc.nombreCategoria, rc.unidadesVendidas, rc.totalGenerado);
            }
        } catch (Exception e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }

    public void mostrarResumenEstadistico() {
        if (ventasPorProducto.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        ventasPorProducto.sort(Comparator.comparingInt(rp -> -rp.unidadesVendidas));
        RegistroProducto masVendido = ventasPorProducto.get(0);
        RegistroProducto menosVendido = ventasPorProducto.get(ventasPorProducto.size() - 1);

        System.out.println("=== PRODUCTO MÁS VENDIDO ===");
        System.out.printf("%s (%s): %d unidades%n", masVendido.nombreProducto, masVendido.codigoProducto, masVendido.unidadesVendidas);

        System.out.println("=== PRODUCTO MENOS VENDIDO ===");
        System.out.printf("%s (%s): %d unidades%n", menosVendido.nombreProducto, menosVendido.codigoProducto, menosVendido.unidadesVendidas);

        // Ordenar categorías
        ventasPorCategoria.sort(Comparator.comparingInt(rc -> -rc.unidadesVendidas));
        RegistroCategoria categoriaTop = ventasPorCategoria.get(0);

        System.out.println("=== CATEGORÍA MÁS VENDIDA ===");
        System.out.printf("%s: %d unidades%n", categoriaTop.nombreCategoria, categoriaTop.unidadesVendidas);
    }
}
