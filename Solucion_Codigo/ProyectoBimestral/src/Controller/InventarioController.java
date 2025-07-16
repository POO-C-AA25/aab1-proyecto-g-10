package Controller;

import Model.Inventario;
import Model.Producto;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Formatter;
import java.util.Locale;
import java.util.Scanner;

public class InventarioController {
    private final String archivoStock = "inventario.csv";
    private final Inventario inventario;
    private final NumberFormat formatoNumero;

    public InventarioController() {
        this.inventario = new Inventario();
        this.formatoNumero = NumberFormat.getNumberInstance(Locale.getDefault());
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void cargarDesdeCSV() {
        File archivo = new File(archivoStock);
        if (!archivo.exists()) return;

        try (Scanner lector = new Scanner(archivo)) {
            int linea = 0;
            while (lector.hasNextLine()) {
                linea++;
                String contenido = lector.nextLine().trim();
                if (contenido.isEmpty() || contenido.startsWith("idProducto")) continue;

                String[] campos = contenido.split(";");
                if (campos.length >= 6) {
                    Producto p = ProductoFactory.crearProductoDesdeCampos(campos, formatoNumero);
                    inventario.registrarProducto(p);
                }
            }
        } catch (Exception ex) {
            System.err.println("[ERROR InventarioController] " + ex.getMessage());
        }
    }

    public void guardarEnCSV() {
        try (Formatter salida = new Formatter(archivoStock)) {
            salida.format("idProducto;nombre;categoria;precioNormal;cantidadStock;fechaCaducidad%n");
            for (Producto p : inventario.obtenerProductos()) {
                salida.format("%s;%s;%s;%.2f;%d;%s%n",
                        p.getCodigoProducto(),
                        p.getDescripcion(),
                        p.getCategoria(),
                        p.getPrecioBase(),
                        p.getUnidadesDisponibles(),
                        p.getVencimiento());
            }
        } catch (Exception ex) {
            System.err.println("[ERROR] Al guardar inventario: " + ex.getMessage());
        }
    }
}
