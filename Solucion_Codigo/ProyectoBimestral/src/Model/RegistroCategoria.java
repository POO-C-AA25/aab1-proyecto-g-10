package Model;

/**
 *
 * @author Mateo Gonzáles y Mateo Rivera
 */
import java.io.Serializable;

public class RegistroCategoria implements Serializable {
    public String nombreCategoria;
    public int unidadesVendidas;
    public double totalGenerado;

    public RegistroCategoria(String categoria) {
        this.nombreCategoria = categoria;
        this.unidadesVendidas = 0;
        this.totalGenerado = 0.0;
    }
}