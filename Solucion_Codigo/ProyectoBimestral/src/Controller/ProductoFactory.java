package Controller;

import Model.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;

public class ProductoFactory {

    public static Producto crearProductoDesdeCampos(String[] campos, NumberFormat formato) {
        if (campos.length < 6) {
            throw new IllegalArgumentException("Campos insuficientes para crear producto");
        }

        String codigo = campos[0].trim();
        String nombre = campos[1].trim();
        String categoria = campos[2].trim();
        double precio;
        int stock;
        LocalDate caducidad;

        try {
            precio = formato.parse(campos[3].trim()).doubleValue();
        } catch (ParseException e1) {
            try {
                precio = Double.parseDouble(campos[3].trim().replace(",", "."));
            } catch (NumberFormatException e2) {
                throw new IllegalArgumentException("Error al parsear precio: " + campos[3]);
            }
        }

        try {
            stock = Integer.parseInt(campos[4].trim());
            caducidad = LocalDate.parse(campos[5].trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al parsear stock o fecha: " + e.getMessage());
        }

        // Lógica para determinar subclase según categoría o condiciones
        if (categoria.equalsIgnoreCase("Alimentación") || categoria.equalsIgnoreCase("Salud")) {
            if (caducidad.isBefore(LocalDate.now().plusDays(15)) || stock > 50) {
                return new ProductoImportado(codigo, nombre, categoria, precio, stock, caducidad);
            } else {
                return new ProductoLocal(codigo, nombre, categoria, precio, stock, caducidad);
            }
        } else if (categoria.equalsIgnoreCase("Vestimenta")) {
            return new ProductoVestimenta(codigo, nombre, categoria, precio, stock, caducidad);
        } else if (categoria.equalsIgnoreCase("Educación")) {
            return new ProductoEducativo(codigo, nombre, categoria, precio, stock, caducidad);
        } else if (categoria.equalsIgnoreCase("Vivienda")) {
            return new ProductoVivienda(codigo, nombre, categoria, precio, stock, caducidad);
        } else {
            // Categoría desconocida, usar genérico
            return new Producto(codigo, nombre, categoria, precio, stock, caducidad);
        }
    }
}
