package Controller;

import Model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Formatter;
import java.util.Locale;
import java.util.Scanner;

public class Controller {

    private Inventario stock;
    private Deducibles gestionDeduccion;
    private EstadisticaVentas analisisVentas;
    private final String archivoStock = "inventario.csv";
    private final String archivoVentas = "facturas.csv";

    private final NumberFormat formatoNumero;

    public Controller() {
        formatoNumero = NumberFormat.getNumberInstance(Locale.getDefault());

        stock = new Inventario();
        cargarInventarioDesdeArchivo();

        gestionDeduccion = new Deducibles();
        analisisVentas = new EstadisticaVentas();
    }   


    public void cargarInventarioDesdeArchivo() {
        File archivo = new File(archivoStock);
        if (!archivo.exists()) {
            try (Formatter creador = new Formatter(archivo)) {
                creador.format("idProducto;nombre;categoria;precioNormal;cantidadStock;fechaCaducidad%n");
            } catch (Exception ex) {
                System.err.println("No se pudo crear archivo inventario: " + ex.getMessage());
            }
            return;
        }

        try (Scanner lector = new Scanner(archivo)) {
            int linea = 0;
            while (lector.hasNextLine()) {
                linea++;
                String contenido = lector.nextLine().trim();
                if (contenido.isEmpty() || contenido.startsWith("idProducto;")) continue;
                try {
                    String[] campos = contenido.split(";");
                    if (campos.length >= 6) {
                        Producto item = new Producto();
                        item.setCodigoProducto(campos[0]);
                        item.setDescripcion(campos[1]);
                        item.setCategoria(campos[2]);
                        item.setPrecioBase(convertirADouble(campos[3]));
                        item.setUnidadesDisponibles(Integer.parseInt(campos[4]));
                        item.setVencimiento(java.time.LocalDate.parse(campos[5]));
                        stock.registrarProducto(item);
                    }
                } catch (Exception error) {
                    System.err.println("Error en línea " + linea + ": " + error.getMessage());
                    System.err.println("Contenido: " + contenido);
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println("No se encontró el archivo de inventario: " + ex.getMessage());
        }
    }
    
    public double convertirADouble(String numeroStr) throws ParseException {
        try {
            return NumberFormat.getNumberInstance(Locale.getDefault()).parse(numeroStr.trim()).doubleValue();
        } catch (ParseException e1) {
            try {
                return Double.parseDouble(numeroStr.trim().replace(',', '.'));
            } catch (NumberFormatException e2) {
                System.out.println("Error al convertir número: " + numeroStr);
                return -1; // o puedes lanzar una RuntimeException
            }
        }
    }


    public void guardarInventario() {
        try (Formatter salida = new Formatter(archivoStock)) {
            salida.format("idProducto;nombre;categoria;precioNormal;cantidadStock;fechaCaducidad%n");
            for (Producto pr : stock.obtenerProductos()) {
                salida.format("%s;%s;%s;%.2f;%d;%s%n",
                        pr.getCodigoProducto(), pr.getDescripcion(), pr.getCategoria(), pr.getPrecioBase(),
                        pr.getUnidadesDisponibles(), pr.getVencimiento().toString());
            }
        } catch (Exception ex) {
            System.err.println("Error guardando inventario: " + ex.getMessage());
        }
    }

    public void mostrarInventarioDisponible() {
        stock.mostrarInventario();
    }

    public void nuevoProducto(Scanner lector) {
        System.out.println("== REGISTRO DE PRODUCTO NUEVO ==");

        System.out.print("Nombre: ");
        String nombreProd = lector.nextLine().trim();

        System.out.print("Categoría: ");
        String tipo = lector.nextLine().trim();

        double precioUnitario = 0;
        while (true) {
            try {
                System.out.print("Precio (usar , o .): ");
                precioUnitario = convertirADouble(lector.nextLine().trim());
                break;
            } catch (ParseException err) {
                System.out.println("Error: " + err.getMessage() + ". Intente otra vez.");
            }
        }

        System.out.print("Cantidad inicial: ");
        int stockInicial = Integer.parseInt(lector.nextLine().trim());

        Producto nuevo = new Producto(nombreProd, tipo, precioUnitario, stockInicial);
        stock.registrarProducto(nuevo);
        guardarInventario();
        System.out.println("[✔] Producto añadido. ID: " + nuevo.getCodigoProducto() 
                + " | Caducidad: " + nuevo.getVencimiento());
    }

    public void generarFactura(Scanner lector) {
        Factura facturaGenerada = new Factura();

        System.out.println("== DATOS DEL CLIENTE ==");
        System.out.print("ID: ");
        String id = lector.nextLine().trim();
        System.out.print("Nombre: ");
        String nombre = lector.nextLine().trim();
        System.out.print("Correo: ");
        String email = lector.nextLine().trim();
        System.out.print("Teléfono: ");
        String telf = lector.nextLine().trim();
        System.out.print("Dirección: ");
        String direccion = lector.nextLine().trim();

        Cliente nuevoCliente = new Cliente(id, nombre, email, telf, direccion);
        facturaGenerada.asignarCliente(nuevoCliente);
        mostrarInventarioDisponible();

        while (true) {
            System.out.print("Ingrese ID producto (o 'e' para terminar): ");
            String codigoBuscado = lector.nextLine().trim();

            if (codigoBuscado.equalsIgnoreCase("e")) {
                break;
            }

            Producto encontrado = stock.buscarPorCodigo(codigoBuscado);
            if (encontrado == null) {
                System.out.println("[ERROR] Producto no encontrado. Intente de nuevo.");
                continue;
            }

            System.out.print("Cantidad a vender: ");
            int unidades = 0;
            try {
                unidades = Integer.parseInt(lector.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("[X] Entrada inválida. Intente otra vez.");
                continue;
            }

            if (unidades <= 0) {
                System.out.println("[X] Cantidad inválida. Debe ser mayor que cero.");
                continue;
            }
            if (unidades > encontrado.getUnidadesDisponibles()) {
                System.out.println("[X] Cantidad excede inventario disponible.");
                continue;
            }

            DetallesFactura detalle = new DetallesFactura(encontrado, unidades, encontrado.getPrecioBase());
            facturaGenerada.añadirDetalle(detalle);

            encontrado.reducirInventario(unidades);
            guardarInventario();
            System.out.println("[✔] Agregado: " + unidades + " x " + encontrado.getDescripcion());

            System.out.print("¿Desea agregar otro producto? (S/N): ");
            if (!lector.nextLine().trim().equalsIgnoreCase("S")) {
                break;
            }
        }


        if (facturaGenerada.getDetalles().isEmpty()) {
            System.out.println("[!] No se generó la factura por falta de productos.");
            return;
        }

        facturaGenerada.calcularMontoBase();
        facturaGenerada.calcularImpuesto(0.12);
        facturaGenerada.calcularMontoFinal(gestionDeduccion);

        System.out.println(facturaGenerada);

        registrarFactura(facturaGenerada);

        for (DetallesFactura item : facturaGenerada.getDetalles()) {
            analisisVentas.agregarDetalleFactura(item);
        }
        analisisVentas.persistirEstadisticasCSV();
    }

    private void registrarFactura(Factura factura) {
        File destino = new File(archivoVentas);
        boolean yaExiste = destino.exists();

        try (Formatter salida = new Formatter(new FileOutputStream(destino, true))) {
            salida.format("# Facturas%n");
            salida.format("# Encabezado: idFactura;fecha;clienteId;clienteNombre;correo;telefono;direccion;subtotal;iva;total%n");

            salida.format("%s;%s;%s;%s;%s;%s;%s;%.2f;%.2f;%.2f%n",
            factura.getCodigoFactura(),
            factura.getFecha(),
            factura.getDatosCliente().getId(),
            factura.getDatosCliente().getNombre(),
            factura.getDatosCliente().getCorreo(),
            factura.getDatosCliente().getTelefono(),
            factura.getDatosCliente().getDireccion(),
            factura.getMontoBase(),
            factura.getImpuesto(),
            factura.getMontoFinal()
        );


            salida.format("# Detalle: idFactura;idProducto;nombreProducto;categoria;cantidad;precioUnitario;subtotalLinea%n");
            for (DetallesFactura detalle : factura.getDetalles()) {
                salida.format("%s;%s;%s;%s;%d;%.2f;%.2f%n",
                factura.getCodigoFactura(),
                detalle.getProducto().getCodigoProducto(),
                detalle.getProducto().getDescripcion(),
                detalle.getProducto().getCategoria(),
                detalle.getUnidadesVendidas(),
                detalle.getPrecioIndividual(),
                detalle.getSubtotal()
            );

            }
            salida.format("%n");
        } catch (Exception ex) {
            System.err.println("No se pudo guardar la factura: " + ex.getMessage());
        }
    }

    public void mostrarResumenEstadistico() {
        analisisVentas.mostrarResumenEstadistico();
    }
}
