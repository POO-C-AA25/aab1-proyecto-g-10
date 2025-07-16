package Model;

import java.io.Serializable;

/**
 * Representa los datos personales y de contacto de un cliente del SuperMaxi Loja.
 * @author Usuario
 */
public class Cliente implements Serializable {
    private String id;         // Cédula o RUC del cliente
    private String nombre;     // Nombre completo
    private String email;      // Dirección de correo electrónico
    private String celular;    // Número telefónico
    private String ubicacion;  // Dirección domiciliaria

    // Constructor vacío
    public Cliente() {
    }

    // Constructor completo
    public Cliente(String id, String nombre, String email, String celular, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.celular = celular;
        this.ubicacion = ubicacion;
    }
    
    public boolean esConsumidorFinal() {
        return false;
    }
    
    public String getTipoCliente() {
        return "GENERAL";
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getCelular() {
        return celular;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    // Representación del cliente como texto legible
    @Override
    public String toString() {
        return String.format("Cédula/RUC: %s | Nombre: %s | Correo: %s | Teléfono: %s | Dirección: %s",
            id, nombre, email, celular, ubicacion);
    }
}
