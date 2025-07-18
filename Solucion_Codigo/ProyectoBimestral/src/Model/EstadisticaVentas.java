package Model;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;

public class EstadisticaVentas implements Serializable {
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

        RegistroProducto prod = buscarProductoRegistrado(codigo);
        if (prod == null) {
            prod = new RegistroProducto(codigo, nombre);
            ventasPorProducto.add(prod);
        }
        prod.unidadesVendidas += cantidad;
        prod.totalGenerado += subtotal;

        RegistroCategoria cat = buscarCategoriaRegistrada(categoria);
        if (cat == null) {
            cat = new RegistroCategoria(categoria);
            ventasPorCategoria.add(cat);
        }
        cat.unidadesVendidas += cantidad;
        cat.totalGenerado += subtotal;
    }

    public RegistroProducto buscarProductoRegistrado(String codigo) {
        for (RegistroProducto rp : ventasPorProducto) {
            if (rp.codigoProducto.equals(codigo)) {
                return rp;
            }
        }
        return null;
    }

    public RegistroCategoria buscarCategoriaRegistrada(String nombreCategoria) {
        for (RegistroCategoria rc : ventasPorCategoria) {
            if (rc.nombreCategoria.equalsIgnoreCase(nombreCategoria)) {
                return rc;
            }
        }
        return null;
    }

    public void persistirEstadisticasCSV() {
        try (Formatter f = new Formatter(new FileOutputStream("datosestadisticas.csv", true))) {
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            f.format(">>> ESTADÍSTICAS INICIADAS EN: " + ahora.format(formato) + "\n");
            f.format("Ventas por Producto%n");
            f.format("codigoProducto;nombreProducto;unidadesVendidas;totalGenerado%n");
            for (RegistroProducto rp : ventasPorProducto) {
                f.format("%s;%s;%d;%.2f%n", rp.codigoProducto, rp.nombreProducto, rp.unidadesVendidas, rp.totalGenerado);
            }
            f.format("%nVentas por Categoría%n");
            f.format("nombreCategoria;unidadesVendidas;totalGenerado%n");
            for (RegistroCategoria rc : ventasPorCategoria) {
                f.format("%s;%d;%.2f%n", rc.nombreCategoria, rc.unidadesVendidas, rc.totalGenerado);
            }
        } catch (Exception e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }

    public ArrayList<RegistroProducto> getVentasPorProductoOrdenadas() {
        ventasPorProducto.sort(Comparator.comparingInt(rp -> -rp.unidadesVendidas));
        return ventasPorProducto;
    }

    public ArrayList<RegistroCategoria> getVentasPorCategoriaOrdenadas() {
        ventasPorCategoria.sort(Comparator.comparingInt(rc -> -rc.unidadesVendidas));
        return ventasPorCategoria;
    }
}