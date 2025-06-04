package Model;

import java.util.ArrayList;

/**
 * Clase que representa un inventario de productos en la tienda Supermaxi.
 * Permite registrar productos, buscarlos por código y mostrar el listado.
 */
public class Inventario {

    // Lista interna donde se almacenan todos los productos del inventario
    private ArrayList<Producto> listaProductos;    
   

    // Constructor: inicializa el inventario como una lista vacía
    public Inventario() {
        listaProductos = new ArrayList<>();
    }

    /**
     * Registra un nuevo producto en el inventario.
     */
    public void registrarProducto(Producto producto) {
        listaProductos.add(producto);
    }

    /**
     * Busca un producto por su código único.
     */
    public Producto buscarPorCodigo(String codigoProducto) {
        for (Producto p : listaProductos) {
            if (p.getCodigoProducto().equalsIgnoreCase(codigoProducto)) {
                return p;
            }
        }
        return null; // No se encontró el producto
    }

    public void mostrarInventario() {
        System.out.println("+----------+--------------------------------+------------+----------+-------+----------+");
        System.out.println("| CÓDIGO   | NOMBRE DEL PRODUCTO             | CATEGORÍA  | PRECIO   | STOCK | CADUCA   |");
        System.out.println("+----------+--------------------------------+------------+----------+-------+----------+");

        for (Producto producto : listaProductos) {
            System.out.println(producto);
        }
        System.out.println("+----------+--------------------------------+------------+----------+-------+----------+");
    }

    /**
     * Devuelve la lista completa de productos (lectura).
     */
    public ArrayList<Producto> obtenerProductos() {
        return listaProductos;
    }
}
