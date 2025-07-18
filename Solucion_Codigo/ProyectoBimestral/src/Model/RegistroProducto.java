package Model;

/**
 *
 * @author Mateo Gonzáles y Mateo Rivera
 */
import java.io.Serializable;

public class RegistroProducto implements Serializable {
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