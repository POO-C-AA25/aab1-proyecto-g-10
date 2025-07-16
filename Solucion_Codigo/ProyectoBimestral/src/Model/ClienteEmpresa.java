package Model;

public class ClienteEmpresa extends Cliente {
    
    public ClienteEmpresa() {
        // Constructor vacío
    }

    public ClienteEmpresa(String id, String nombre, String email, String celular, String ubicacion) {
        super(id, nombre, email, celular, ubicacion);
    }

    @Override
    public String getTipoCliente() {
        return "EMPRESA";
    }
}
