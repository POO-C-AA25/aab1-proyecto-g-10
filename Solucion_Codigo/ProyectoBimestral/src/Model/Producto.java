package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

/**
 * Clase abstracta que representa un producto general del SuperMaxi.
 * Cada producto tiene código, descripción, categoría (como String), precio base, stock y fecha de vencimiento aleatoria.
 */
public abstract class Producto implements Serializable {
    private String codigoProducto;   // Código único para identificar el producto
    private String descripcion;      // Descripción del producto
    private String categoria;        // Categoría del producto (Vivienda, Educación, etc.) como String
    private double precioBase;       // Precio base sin descuentos
    private int unidadesDisponibles; // Stock actual
    private LocalDate vencimiento;   // Fecha de vencimiento aleatoria por defecto
    
    public Producto() {
        // Constructor vacío
    }
    
    // Constructor para productos, genera fecha de vencimiento aleatoria entre hoy y 90 días después
    public Producto(String descripcion, String categoria, double precioBase, int unidadesDisponibles) {
        this.codigoProducto = UUID.randomUUID().toString().substring(0, 8);
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precioBase = precioBase;
        this.unidadesDisponibles = unidadesDisponibles;
        this.vencimiento = generarFechaVencimientoAleatoria();
    }

    // Constructor adicional para permitir pasar fecha de vencimiento específica
    public Producto(String descripcion, String categoria, double precioBase, int unidadesDisponibles, LocalDate vencimiento) {
        this.codigoProducto = UUID.randomUUID().toString().substring(0, 8);
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precioBase = precioBase;
        this.unidadesDisponibles = unidadesDisponibles;
        this.vencimiento = vencimiento;
    }

    private LocalDate generarFechaVencimientoAleatoria() {
        int diasExtra = new Random().nextInt(90) + 1;
        return LocalDate.now().plusDays(diasExtra);
    }

    // Método abstracto para obtener información adicional de cada tipo de producto
    public abstract String obtenerInformacionExtra();

    // Método abstracto para calcular precio final con descuentos o recargos según el tipo
    public abstract double calcularPrecioFinal();

    // Reduce stock si hay suficientes unidades
    public boolean reducirInventario(int cantidad) {
        if (cantidad <= unidadesDisponibles) {
            unidadesDisponibles -= cantidad;
            return true;
        }
        return false;
    }

    // Añade unidades al stock
    public void reponerInventario(int cantidad) {
        unidadesDisponibles += cantidad;
    }

    // Getters y setters
    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public int getUnidadesDisponibles() {
        return unidadesDisponibles;
    }

    public LocalDate getVencimiento() {
        return vencimiento;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public void setUnidadesDisponibles(int unidadesDisponibles) {
        this.unidadesDisponibles = unidadesDisponibles;
    }

    public void setVencimiento(LocalDate vencimiento) {
        this.vencimiento = vencimiento;
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-25s | %-12s | %8.2f | %4d | %10s",
                codigoProducto, descripcion, categoria, precioBase, unidadesDisponibles,
                (vencimiento == null ? "N/A" : vencimiento.toString()));
    }
}