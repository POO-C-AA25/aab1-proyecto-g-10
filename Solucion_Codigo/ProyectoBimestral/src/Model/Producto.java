package Model;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

public class Producto {    
    private String codigoProducto;   // Código único para identificar el producto
    private String descripcion;      // Descripción del artículo
    private String categoria;             // Tipo de categoría a la que pertenece
    private double precioBase;       // Precio de venta al público   
    private int unidadesDisponibles;           // Unidades disponibles en el inventario    
    private LocalDate vencimiento;             // Fecha en la que el producto expira

    public Producto() {
        // Constructor vacío
    }

    public Producto(String descripcion, String categoria, double precioBase, int unidadesDisponibles) {
        this.codigoProducto = UUID.randomUUID().toString().substring(0, 8);
        this.descripcion    = descripcion;
        this.categoria      = categoria;
        this.precioBase     = precioBase;
        this.unidadesDisponibles       = unidadesDisponibles;

        // Elegir una fecha de vencimiento entre hoy y 90 días después
        int diasExtra = new Random().nextInt(90) + 1;
        this.vencimiento = LocalDate.now().plusDays(diasExtra);
    }

    // Reduce el número de unidades disponibles si hay suficientes
    public boolean reducirInventario(int cantidadSolicitada) {
        if (cantidadSolicitada <= unidadesDisponibles) {
            this.unidadesDisponibles -= cantidadSolicitada;
            return true;
        }
        return false;
    }

    // Aumenta el stock con nuevas unidades
    public void reponerInventario(int cantidad) {
        this.unidadesDisponibles += cantidad;
    }

    // Métodos de acceso
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
    
    // Métodos setter
    public void setCodigoProducto(String codigo) {
        this.codigoProducto = codigo;
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

    // Representación del producto como una línea de texto tabulada
    @Override
    public String toString() {
        return String.format(
            "%-8s | %-25s | %-12s | %8.2f | %4d | %10s",
            this.codigoProducto, 
            this.descripcion, 
            this.categoria, 
            this.precioBase, 
            this.unidadesDisponibles, 
            this.vencimiento.toString()
        );
    }
}
