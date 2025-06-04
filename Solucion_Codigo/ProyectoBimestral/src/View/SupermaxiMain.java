package View;

import Controller.Controller;
import java.util.Scanner;

/**
 * Punto de inicio de la aplicación de facturación.
 * Despliega un menú con opciones operativas: visualizar productos,
 * ingresar nuevos, emitir facturas y consultar estadísticas.
 */
public class SupermaxiMain {
    public static void main(String[] args) {
        Controller gestor = new Controller();
        Scanner entrada = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n+------------------------------------------------------+");
            System.out.println("|              SUPERMAXI LOJA - FACTURACIÓN            |");
            System.out.println("+------------------------------------------------------+");
            System.out.println("| 1. Ver productos disponibles                         |");
            System.out.println("| 2. Registrar un nuevo producto                       |");
            System.out.println("| 3. Crear una factura                                 |");
            System.out.println("| 4. Consultar ventas                                  |");
            System.out.println("| 5. Salir del sistema                                 |");
            System.out.println("+------------------------------------------------------+");
            System.out.print(">> Seleccione una opción del menú: ");

            String opcion = entrada.nextLine().trim();

            switch (opcion) {
                case "1":
                    gestor.mostrarInventarioDisponible();
                    break;

                case "2":
                    System.out.print("Ingrese contraseña de administrador: ");
                    String clave = entrada.nextLine().trim();

                    if (clave.equals("1150525077")) {
                        gestor.nuevoProducto();
                    } else {
                        System.out.println("Acceso denegado. Contraseña incorrecta.");
                        continuar = false; // Finaliza el programa si la clave es incorrecta
                    }
                    break;

                case "3":
                    gestor.generarFactura();
                    break;

                case "4":
                    gestor.mostrarResumenEstadistico();
                    break;

                case "5":
                    continuar = false;
                    break;

                default:
                    System.out.println("[Error] Opción no reconocida. Intente de nuevo.");
            }
        }
        System.out.println("Aplicación finalizada.");
        entrada.close();
    }
}
