package Model;

/**
 * Clase que define los datos personales y de contacto de un cliente.
 * Utilizada para asociar un comprador a una factura dentro del sistema.
 */
public class Cliente {
    private String id;     // Identificador único del cliente
    private String nombre;     // Nombre completo
    private String email;      // Dirección de correo electrónico
    private String celular;    // Número telefónico
    private String ubicacion;  // Domicilio del cliente

    public Cliente() {
        // Constructor vacío
    }

    public Cliente(String id, String nombre, String email, String celular, String ubicacion) {
        this.id        = id;
        this.nombre    = nombre;
        this.email     = email;
        this.celular   = celular;
        this.ubicacion = ubicacion;
    }
    
    // Métodos para acceder a los datos del cliente
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return email;
    }

    public String getTelefono() {
        return celular;
    }

    public String getDireccion() {
        return ubicacion;
    }

    // Devuelve una representación legible del cliente
    @Override
    public String toString() {
        return String.format("Cédula/RUC: %s | Nombre: %s | Correo: %s | Teléfono: %s | Dirección: %s",
            id, nombre, email, celular, ubicacion);
    }
}
