package Controller;

import Model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
                        double precioLeido = -1;
                        String precioStr = campos[3].trim();
                        try {
                            precioLeido = formatoNumero.parse(precioStr).doubleValue();
                        } catch (ParseException e1) {
                            try {
                                precioLeido = Double.parseDouble(precioStr.replace(',', '.'));
                            } catch (NumberFormatException e2) {
                                System.out.println("Error al convertir precio en línea " + linea + ": " + precioStr);
                            }
                        }
                        item.setPrecioBase(precioLeido);

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

    public void guardarInventario() {
        try (Formatter salida = new Formatter(archivoStock)) {
            salida.format("idProducto;nombre;categoria;precioNormal;cantidadStock;fechaCaducidad%n");
            for (Producto pr : stock.obtenerProductos()) {
                salida.format("%s;%s;%s;%.2f;%d;%s%n",
                        pr.getCodigoProducto(),
                        pr.getDescripcion(),
                        pr.getCategoria(),
                        pr.getPrecioBase(),
                        pr.getUnidadesDisponibles(),
                        pr.getVencimiento().toString()
                );
            }
        } catch (Exception ex) {
            System.err.println("Error guardando inventario: " + ex.getMessage());
        }
    }

    public void mostrarInventarioDisponible() {
        stock.mostrarInventario();
    }

    public void nuevoProducto() {
        Scanner lector = new Scanner(System.in);
        System.out.println("== REGISTRO DE PRODUCTO NUEVO ==");

        System.out.print("Nombre: ");
        String nombreProd = lector.nextLine().trim();

        System.out.print("Categoría: ");
        String tipo = lector.nextLine().trim();

        double precioUnitario = -1;
        while (true) {
            System.out.print("Precio (usar , o .): ");
            String entradaPrecio = lector.nextLine().trim();

            // Parseo inline del precio
            try {
                precioUnitario = formatoNumero.parse(entradaPrecio).doubleValue();
                break;
            } catch (ParseException e1) {
                try {
                    precioUnitario = Double.parseDouble(entradaPrecio.replace(',', '.'));
                    break;
                } catch (NumberFormatException e2) {
                    System.out.println("Error al convertir número: " + entradaPrecio + ". Intente de nuevo.");
                }
            }
        }

        System.out.print("Cantidad inicial: ");
        int stockInicial;
        try {
            stockInicial = Integer.parseInt(lector.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[X] Cantidad inválida. Se asigna 0.");
            stockInicial = 0;
        }

        Producto nuevo = new Producto(nombreProd, tipo, precioUnitario, stockInicial);
        stock.registrarProducto(nuevo);
        guardarInventario();
        System.out.println("[CORRECT] Producto añadido. ID: " + nuevo.getCodigoProducto()
                + " | Caducidad: " + nuevo.getVencimiento());
    }

    public void generarFactura() {
        Scanner lector = new Scanner(System.in);
        boolean continuar = true;
        Factura facturaGenerada = new Factura();
        
        mostrarInventarioDisponible();

        while (continuar) {
            System.out.print("Ingrese el ID producto: ");
            String codigoBuscado = lector.nextLine().trim();

            Producto encontrado = stock.buscarPorCodigo(codigoBuscado);
            if (encontrado == null) {
                System.out.println("[ERROR] Producto no encontrado. Intente de nuevo.");
                continue;
            }

            System.out.print("Cantidad a vender: ");
            int unidades;
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

            DetallesFactura detalle = new DetallesFactura(
                    encontrado,
                    unidades,
                    encontrado.getPrecioBase()
            );
            facturaGenerada.añadirDetalle(detalle);

            encontrado.reducirInventario(unidades);
            guardarInventario();
            System.out.println("[Correcto] Agregado: " + unidades + " x " + encontrado.getDescripcion());

            System.out.print("¿Desea agregar otro producto? (S/N): ");
            if (!lector.nextLine().trim().equalsIgnoreCase("S")) {
                continuar = false;
            }
        }
        
        System.out.println("\n+++++ INGRESO DE DATOS DEL CLIENTE +++++");
        // Validar CÉDULA/RUC (10 o 13 dígitos)
        String id;
        do {
            System.out.print("CÉDULA/RUC (10 o 13 dígitos): ");
            id = lector.nextLine().trim();
            if (!(id.length() == 10 || id.length() == 13)) {
                System.out.println("[X] Debe tener exactamente 10 o 13 caracteres. Intente de nuevo.");
            }
        } while (!(id.length() == 10 || id.length() == 13));

        // Validar nombres y apellidos (no vacío)
        String nombre;
        do {
            System.out.print("Nombres y Apellidos: ");
            nombre = lector.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("[X] Este campo no puede quedar vacío. Intente de nuevo.");
            }
        } while (nombre.isEmpty());

        // Validar correo electrónico (dominios permitidos)
        String email;
        do {
            System.out.print("Correo electrónico (ej. usuario@gmail.com): ");
            email = lector.nextLine().trim();
            String dominio = "";
            if (email.contains("@")) {
                dominio = email.substring(email.indexOf("@") + 1).toLowerCase();
            }
            boolean dominioValido = dominio.endsWith("gmail.com") ||
                                    dominio.endsWith("hotmail.com") ||
                                    dominio.endsWith("outlook.com") ||
                                    dominio.endsWith("yahoo.com") ||
                                    dominio.endsWith("gmail.com.ec") ||
                                    dominio.endsWith("hotmail.com.ec") ||
                                    dominio.endsWith("outlook.com.ec") ||
                                    dominio.endsWith("yahoo.com.ec");
            if (!dominioValido) {
                System.out.println("[X] Dominio no válido. Use @gmail.com, @hotmail.com, @outlook.com, @yahoo.com o variantes .ec");
            }
        } while (!(email.contains("@") &&
                   ( email.endsWith("gmail.com") || email.endsWith("hotmail.com") ||
                     email.endsWith("outlook.com") || email.endsWith("yahoo.com") ||
                     email.endsWith("gmail.com.ec") || email.endsWith("hotmail.com.ec") ||
                     email.endsWith("outlook.com.ec") || email.endsWith("yahoo.com.ec") )));

        // Validar Teléfono/Celular (7, 9 o 10 dígitos)
        String telf;
        do {
            System.out.print("Teléfono/Celular (7, 9 o 10 dígitos): ");
            telf = lector.nextLine().trim();
            if (!(telf.length() == 7 || telf.length() == 9 || telf.length() == 10)) {
                System.out.println("[X] Debe tener exactamente 7, 9 o 10 dígitos. Intente de nuevo.");
            }
        } while (!(telf.length() == 7 || telf.length() == 9 || telf.length() == 10));

        // Pedir dirección sin validación adicional
        System.out.print("Dirección: ");
        String direccion = lector.nextLine().trim();


        Cliente nuevoCliente = new Cliente(id, nombre, email, telf, direccion);
        facturaGenerada.asignarCliente(nuevoCliente);

        if (facturaGenerada.getDetalles().isEmpty()) {
            System.out.println("[ATENCION] No se generó la factura por falta de productos.");
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
        // Persistir todas las estadísticas (todas, no solo de hoy)
        analisisVentas.persistirEstadisticasCSV();
    }    

    public void registrarFactura(Factura factura) {

        try (Formatter salida = new Formatter(new FileOutputStream(archivoVentas, true))) {
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            salida.format(">>> FACTURACION INICIADA EN: " + ahora.format(formato) + "\n");
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
            System.out.println("[INFO] Factura registrada y guardada en " + archivoVentas);
        } catch (FileNotFoundException ex) {
            System.err.println("No se ha encontrado el archivo: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("No se pudo guardar la factura: " + ex.getMessage());
        }
    }

    public void mostrarResumenEstadistico() {
        analisisVentas.mostrarResumenEstadistico();
    }
}
