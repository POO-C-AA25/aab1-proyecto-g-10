package Model;

public class ClientePersonaNatural extends Cliente {

    public ClientePersonaNatural() {
        // Constructor vacío
    }
    
    public ClientePersonaNatural(String id, String nombre, String email, String celular, String ubicacion) {
        super(id, nombre, email, celular, ubicacion);
    }

    @Override
    public String getTipoCliente() {
        return "NATURAL";
    }
}
