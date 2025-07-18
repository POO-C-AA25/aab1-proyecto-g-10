package View;

import Model.Producto;
import java.util.ArrayList;

// Antes (incorrecto, si esperaba Inventario):
// public static void mostrarInventario(Inventario inventario)

public class InventarioView {
    public static void mostrarInventario(ArrayList<Producto> productos) {
        System.out.println("+----------+--------------------------------+------------+----------+-------+----------+");
        System.out.println("| CÓDIGO   | NOMBRE DEL PRODUCTO             | CATEGORÍA  | PRECIO   | STOCK | CADUCA   |");
        System.out.println("+----------+--------------------------------+------------+----------+-------+----------+");

        for (Producto p : productos) {
            System.out.println(p);
        }

        System.out.println("+----------+--------------------------------+------------+----------+-------+----------+");
    }
}

