package Model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author Mateo Gonzáles y Mateo Rivera
 */
public class ProductoPerecible extends Producto{
    private LocalDate vencimiento;   // Fecha de vencimiento aleatoria por defecto
    
    public ProductoPerecible() {
        // Constructor vacío
    }
    
    public ProductoPerecible(String descripcion, String categoria, double precioBase, int unidadesDisponibles, String etiqueta, LocalDate vencimiento) {
        super(descripcion, categoria, precioBase, unidadesDisponibles, etiqueta);
        this.vencimiento = vencimiento;
    }

    // Indica si el producto está a 7 o menos días de vencer
    public boolean estaProximoAVencer() {
        LocalDate hoy = LocalDate.now();
        LocalDate vencimiento = getVencimiento();
        if (vencimiento != null) {
            long dias = ChronoUnit.DAYS.between(hoy, vencimiento);
            return dias >= 0 && dias <= 7;
        }
        return false;
    }    
    
    @Override
    public String obtenerInformacionExtra() {
        return "Perecible - Vence el: " + (getVencimiento() != null ? getVencimiento().toString() : "No definido");
    }

    @Override
    public double calcularPrecioFinal() {
        if (estaProximoAVencer()) {
            return getPrecioBase() * 0.7;  // 30% descuento
        }
        return getPrecioBase();
    }
    
    public LocalDate getVencimiento() {
        return vencimiento;
    }
}