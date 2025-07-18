package Controller;

import Model.*;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;

/**
 * Clase auxiliar que permite crear instancias las subclases de Producto
 * a partir de los campos leídos de un archivo CSV.
 */
public class ProductoFactory {

    public Producto crearProductoDesdeCampos(String[] campos, NumberFormat formato) {
        if (campos.length < 6) {
            System.out.println("Campos insuficientes para crear producto");
            return null;
        }

        String descripcion = campos[0].trim();
        String categoria = campos[1].trim();
        double precio;
        int stock;
        LocalDate vencimiento;
        String etiqueta = campos[5].trim();

        try {
            precio = formato.parse(campos[2].trim()).doubleValue();
        } catch (ParseException e1) {
            try {
                precio = Double.parseDouble(campos[3].trim().replace(",", "."));
            } catch (NumberFormatException e2) {
                throw new IllegalArgumentException("Error al parsear precio: " + campos[3]);
            }
        }

        try {
            stock = Integer.parseInt(campos[3].trim());
            vencimiento = LocalDate.parse(campos[4].trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al parsear stock o fecha: " + e.getMessage());
        }

        if (categoria.equalsIgnoreCase("Alimentación") || categoria.equalsIgnoreCase("Salud")) {
            return new ProductoPerecible(descripcion, categoria, precio, stock, etiqueta, vencimiento);
        }
        if (categoria.equalsIgnoreCase("Vestimenta") || categoria.equalsIgnoreCase("Vivienda")) {
            if (etiqueta.equalsIgnoreCase("Local")) {
                return new ProductoLocal(descripcion, categoria, precio, stock, etiqueta);
            } else {
                return new ProductoImportado(descripcion, categoria, precio, stock, etiqueta);
            }
        }
        if (categoria.equalsIgnoreCase("Educación")) {
            return new ProductoNoPerecible(descripcion, categoria, precio, stock, etiqueta);
        }

        return new Producto(descripcion, categoria, precio, stock, etiqueta); // producto generico, crear clase producto generico
    }
}
