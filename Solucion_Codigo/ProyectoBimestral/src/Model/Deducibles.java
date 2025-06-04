package Model;

public class Deducibles {

    // Determina un porcentaje según la categoría del producto
    public double obtenerPorcentaje(String tipo) {
        String categoria = tipo.trim().toUpperCase();
        switch (categoria) {
            case "ALIMENTACION": 
                return 0.15;
            case "EDUCACION":    
                return 0.20;
            case "VIVIENDA":     
                return 0.10;
            case "VESTIMENTA":   
                return 0.18;
            case "SALUD":        
                return 0.22;
            default:             
                return 0.0;             // En caso de ser una categoría diferente
        }
    }

    // Calcula la cantidad que puede ser deducida
    public double calcularMontoDeducible(String categoria, double valor) {
        return obtenerPorcentaje(categoria) * valor;
    }
}
