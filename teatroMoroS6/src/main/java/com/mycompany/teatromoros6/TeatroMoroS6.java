package com.mycompany.teatromoroS6;

import java.util.Scanner;

public class TeatroMoroS6 {

    // Constantes (estáticas)
    private static final String NOMBRE_TEATRO = "Teatro Moro";
    private static final int MAX_ENTRADAS = 100;
    private static final int ASIENTOS_POR_UBICACION = 10;
    private static final double DESCUENTO_ESTUDIANTE = 0.10; // 10%
    private static final double DESCUENTO_TERCERA_EDAD = 0.15; // 15%
    private static final long TIEMPO_RESERVA = 300000; // 5 minutos

    // Precios configurables (estáticas)
    private static double PRECIO_VIP = 50000.0;
    private static double PRECIO_PLATEA = 30000.0;
    private static double PRECIO_GENERAL = 15000.0;

    // Variables estáticas para estadísticas globales
    private static int entradasDisponibles = MAX_ENTRADAS;
    private static int contadorVentas = 0;
    private static int contadorBoletasVip = 0;
    private static int contadorBoletasPlatea = 0;
    private static int contadorBoletasGeneral = 0;
    private static int contadorReservasVip = 0;
    private static int contadorReservasPlatea = 0;
    private static int contadorReservasGeneral = 0;

    // Asientos (O=libre, X=ocupado)
    private static char[] asientosVip = new char[ASIENTOS_POR_UBICACION];
    private static char[] asientosPlatea = new char[ASIENTOS_POR_UBICACION];
    private static char[] asientosGeneral = new char[ASIENTOS_POR_UBICACION];

    // Variables de instancia para información de ventas
    private String[] codigosVentas;
    private String[] ubicacionesVentas;
    private String[] asientosVentas;
    private String[] tiposClienteVentas;
    private double[] preciosVentas;
    private int[] cantidadesVentas;
    private int totalVentas;

    // Variables de instancia para reservas
    private String[] codigosReservas;
    private String[] ubicacionesReservas;
    private String[] asientosReservas;
    private String[] tiposClienteReservas;
    private int[] cantidadesReservas;
    private long[] tiemposReservas;
    private int totalReservas;

    // Constructor para inicializar variables de instancia
    public TeatroMoroS6() {
        codigosVentas = new String[MAX_ENTRADAS];
        ubicacionesVentas = new String[MAX_ENTRADAS];
        asientosVentas = new String[MAX_ENTRADAS];
        tiposClienteVentas = new String[MAX_ENTRADAS];
        preciosVentas = new double[MAX_ENTRADAS];
        cantidadesVentas = new int[MAX_ENTRADAS];
        totalVentas = 0;

        codigosReservas = new String[MAX_ENTRADAS];
        ubicacionesReservas = new String[MAX_ENTRADAS];
        asientosReservas = new String[MAX_ENTRADAS];
        tiposClienteReservas = new String[MAX_ENTRADAS];
        cantidadesReservas = new int[MAX_ENTRADAS];
        tiemposReservas = new long[MAX_ENTRADAS];
        totalReservas = 0;
    }

    // Inicializar asientos (estático)
    static {
        for (int i = 0; i < ASIENTOS_POR_UBICACION; i++) {
            asientosVip[i] = 'O';
            asientosPlatea[i] = 'O';
            asientosGeneral[i] = 'O';
        }
    }

    // Método main
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TeatroMoroS6 teatro = new TeatroMoroS6();
        boolean salir = false;

        System.out.println("Bienvenido al sistema de reservas del " + NOMBRE_TEATRO);

        while (!salir) {
            teatro.limpiarReservasExpiradas();
            System.out.println("\n=== Menú de ventas ===");
            System.out.println("1. Reservar entradas");
            System.out.println("2. Comprar entradas");
            System.out.println("3. Modificar venta o reserva");
            System.out.println("4. Imprimir boleta");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = leerNumero(scanner, 1, 5);
            switch (opcion) {
                case 1:
                    teatro.reservarEntradas(scanner);
                    break;
                case 2:
                    teatro.comprarEntradas(scanner);
                    break;
                case 3:
                    teatro.modificarVentaOReserva(scanner);
                    break;
                case 4:
                    teatro.imprimirBoleta(scanner);
                    break;
                case 5:
                    salir = true;
                    System.out.println("Gracias por usar el sistema del Teatro Moro. ¡Hasta pronto!");
                    break;
            }
        }
        scanner.close();
    }

    // Leer un número válido entre min y max
    private static int leerNumero(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int numero = scanner.nextInt();
                scanner.nextLine();
                if (numero >= min && numero <= max) {
                    return numero;
                }
                System.out.print("Opción no válida, ingrese un número entre " + min + " y " + max + ": ");
            } catch (Exception e) {
                System.out.print("Entrada no válida, ingrese un número: ");
                scanner.nextLine();
            }
        }
    }

    // Validar respuesta sí/no
    private static boolean esSi(String respuesta) {
        return respuesta.equalsIgnoreCase("S") || respuesta.equalsIgnoreCase("Si");
    }

    // Mostrar mapa de asientos
    private static void mostrarMapaAsientos(String ubicacion) {
        System.out.println("Mapa de asientos (O = libre, X = ocupado) para " + ubicacion + ":");
        System.out.print("    ");
        for (int i = 1; i <= ASIENTOS_POR_UBICACION; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.print(ubicacion.toUpperCase() + ": ");
        char[] asientos;
        if (ubicacion.equals("vip")) {
            asientos = asientosVip;
        } else if (ubicacion.equals("platea")) {
            asientos = asientosPlatea;
        } else {
            asientos = asientosGeneral;
        }
        for (char asiento : asientos) {
            System.out.print(asiento + " ");
        }
        System.out.println();
    }

    // Validar ubicación
    private static boolean esUbicacionValida(String ubicacion) {
        return ubicacion.equals("vip") || ubicacion.equals("platea") || ubicacion.equals("general");
    }

    // Validar tipo de cliente
    private static boolean esTipoClienteValido(String tipo) {
        return tipo.equals("normal") || tipo.equals("estudiante") || tipo.equals("tercera edad");
    }

    // Validar asiento (ej. VIP1)
    private static boolean validarAsiento(String asiento, String ubicacion) {
        String prefix = ubicacion.toUpperCase();
        if (!asiento.startsWith(prefix) || asiento.length() <= prefix.length()) {
            return false;
        }
        try {
            int num = Integer.parseInt(asiento.substring(prefix.length()));
            return num >= 1 && num <= ASIENTOS_POR_UBICACION;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Verificar si el asiento está libre
    private boolean esAsientoDisponible(String asiento) {
        for (int i = 0; i < totalVentas; i++) {
            if (asientosVentas[i] != null && asientosVentas[i].equals(asiento)) {
                return false;
            }
        }
        for (int i = 0; i < totalReservas; i++) { // DEBUG - RESERVA A COMPRA 1: Verificar reserva existente y no expirada
            
            if (asientosReservas[i] != null && asientosReservas[i].equals(asiento) && !esReservaExpirada(i)) {
                System.out.println("Reserva existente para asiento: " + asiento + ", código: " + codigosReservas[i]);
                return false;
            }
        }
        return true;
    }

    // Marcar asiento como ocupado (X) o libre (O)
    private static void marcarAsiento(String asiento, char estado) {
        String ubicacion = asiento.startsWith("VIP") ? "vip"
                : asiento.startsWith("PLATEA") ? "platea" : "general";
        int num = Integer.parseInt(asiento.substring(ubicacion.toUpperCase().length())) - 1;
        char[] asientos = ubicacion.equals("vip") ? asientosVip
                : ubicacion.equals("platea") ? asientosPlatea : asientosGeneral;
        asientos[num] = estado;
    }

    // Verificar si una reserva está expirada
    private boolean esReservaExpirada(int indice) {
        return (System.currentTimeMillis() - tiemposReservas[indice]) > TIEMPO_RESERVA;
    }

    // Limpiar reservas expiradas
    private void limpiarReservasExpiradas() {
        for (int i = 0; i < totalReservas; i++) {
            if (esReservaExpirada(i)) {
                marcarAsiento(asientosReservas[i], 'O');
                // Mover las reservas posteriores
                for (int j = i; j < totalReservas - 1; j++) {
                    codigosReservas[j] = codigosReservas[j + 1];
                    ubicacionesReservas[j] = ubicacionesReservas[j + 1];
                    asientosReservas[j] = asientosReservas[j + 1];
                    tiposClienteReservas[j] = tiposClienteReservas[j + 1];
                    cantidadesReservas[j] = cantidadesReservas[j + 1];
                    tiemposReservas[j] = tiemposReservas[j + 1];
                }
                totalReservas--;
                i--;
            }
        }
    }

    // Reservar entradas
    private void reservarEntradas(Scanner scanner) {
        String ubicacion, nombreAsiento, tipoCliente;
        int cantidad;

        // Pedir ubicación
        while (true) {
            System.out.print("Ubicación (VIP/Platea/General): ");
            ubicacion = scanner.nextLine().toLowerCase();
            if (esUbicacionValida(ubicacion)) {
                break;
            }
            System.out.println("Ubicación no válida, ingrese VIP, Platea o General.");
        }

        // Pedir asiento
        while (true) {
            mostrarMapaAsientos(ubicacion);
            System.out.print("Asiento (ej. " + ubicacion.toUpperCase() + "1): ");
            nombreAsiento = scanner.nextLine().toUpperCase();
            if (!validarAsiento(nombreAsiento, ubicacion)) { // DEBUG - ASIENTO 1: Verificar formato de asiento ingresado
                System.out.println("Formato no válido: " + nombreAsiento);
                System.out.println("Asiento no válido, ingrese " + ubicacion.toUpperCase() + "[1-10].");
                continue;
            }
            
            if (!esAsientoDisponible(nombreAsiento)) { // DEBUG - ASIENTO 2: Verificar disponibilidad del asiento antes de reservar
                System.out.println("No disponible: " + nombreAsiento);
                System.out.println("Asiento ya ocupado, elija otro.");
                continue;
            }
            
            System.out.println("Asiento válido y disponible: " + nombreAsiento); // DEBUG - ASIENTO 3: Asiento seleccionado y validado correctamente
            break;
        }

        // Pedir tipo de cliente
        while (true) {
            System.out.print("Tipo de cliente (Normal/Estudiante/Tercera Edad): ");
            tipoCliente = scanner.nextLine().toLowerCase();
            if (esTipoClienteValido(tipoCliente)) {
                break;
            }
            System.out.println("Tipo no válido, ingrese Normal, Estudiante o Tercera Edad.");
        }

        // Pedir cantidad
        while (true) {
            System.out.print("Cantidad de entradas (máximo 10): ");
            try {
                cantidad = scanner.nextInt();
                scanner.nextLine();
                if (cantidad <= 0 || cantidad > ASIENTOS_POR_UBICACION || cantidad > entradasDisponibles) {
                    System.out.println("Cantidad no válida, ingrese entre 1 y "
                            + Math.min(ASIENTOS_POR_UBICACION, entradasDisponibles) + ".");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Ingrese un número válido.");
                scanner.nextLine();
            }
        }

        // Generar código de reserva
        String codigoReserva;
        if (ubicacion.equals("vip")) {
            contadorReservasVip++;
            codigoReserva = "R-VIP-" + String.format("%03d", contadorReservasVip);
        } else if (ubicacion.equals("platea")) {
            contadorReservasPlatea++;
            codigoReserva = "R-PLA-" + String.format("%03d", contadorReservasPlatea);
        } else {
            contadorReservasGeneral++;
            codigoReserva = "R-GEN-" + String.format("%03d", contadorReservasGeneral);
        }

        // Guardar reserva
        codigosReservas[totalReservas] = codigoReserva;
        ubicacionesReservas[totalReservas] = ubicacion;
        asientosReservas[totalReservas] = nombreAsiento;
        tiposClienteReservas[totalReservas] = tipoCliente;
        cantidadesReservas[totalReservas] = cantidad;
        tiemposReservas[totalReservas] = System.currentTimeMillis();
        totalReservas++;

        marcarAsiento(nombreAsiento, 'X');
        System.out.println("Reserva creada. Código: " + codigoReserva + ". Tiene 5 minutos para comprar.");
    }

    // Comprar entradas
    private void comprarEntradas(Scanner scanner) {
        String ubicacion, nombreAsiento, tipoCliente, codigoBoleta;
        int cantidad;
        double precioBase = 0.0, descuento = 0.0;

        // Preguntar si es desde reserva
        System.out.println("¿Compra desde una reserva? (sí/no): ");
        String desdeReserva = scanner.nextLine();
        boolean usarReserva = esSi(desdeReserva);

        if (usarReserva) {
            // Buscar reserva
            int indiceReserva = -1;
            while (true) {
                System.out.print("Código de reserva (ej. R-VIP-001) o 'salir': ");
                String codigo = scanner.nextLine().toUpperCase();
                if (codigo.equals("SALIR")) {
                    System.out.println("Compra cancelada.");
                    return;
                }
                for (int i = 0; i < totalReservas; i++) { // DEBUG - RESERVA A COMPRA 2: Verificar código de reserva y estado (expirado o no)
                    if (codigosReservas[i] != null && codigosReservas[i].equals(codigo)) {
                        if (!esReservaExpirada(i)) {
                            System.out.println("Reserva válida encontrada, código: " + codigo + ", asiento: " + asientosReservas[i]);
                            indiceReserva = i;
                            break;
                        } else {
                            System.out.println("Reserva expirada, código: " + codigo);
                        }
                    }
                }
                if (indiceReserva != -1) {
                    break;
                }
                System.out.println("Reserva no encontrada o expirada.");
            }

            ubicacion = ubicacionesReservas[indiceReserva];
            nombreAsiento = asientosReservas[indiceReserva];
            tipoCliente = tiposClienteReservas[indiceReserva];
            cantidad = cantidadesReservas[indiceReserva];

            // Eliminar reserva
            for (int i = indiceReserva; i < totalReservas - 1; i++) {
                codigosReservas[i] = codigosReservas[i + 1];
                ubicacionesReservas[i] = ubicacionesReservas[i + 1];
                asientosReservas[i] = asientosReservas[i + 1];
                tiposClienteReservas[i] = tiposClienteReservas[i + 1];
                cantidadesReservas[i] = cantidadesReservas[i + 1];
                tiemposReservas[i] = tiemposReservas[i + 1];
            }
            totalReservas--;
        } else {
            // Pedir ubicación
            while (true) {
                System.out.print("Ubicación (VIP/Platea/General): ");
                ubicacion = scanner.nextLine().toLowerCase();
                if (esUbicacionValida(ubicacion)) {
                    break;
                }
                System.out.println("Ubicación no válida, ingrese VIP, Platea o General.");
            }

            // Pedir asiento
            while (true) {
                mostrarMapaAsientos(ubicacion);
                System.out.print("Asiento (ej. " + ubicacion.toUpperCase() + "1): ");
                nombreAsiento = scanner.nextLine().toUpperCase();
                if (!validarAsiento(nombreAsiento, ubicacion)) {
                    System.out.println("Formato no válido: " + nombreAsiento);  // DEBUG - ASIENTO 1: Verificar formato de asiento ingresado (compra directa)
                    System.out.println("Asiento no válido, ingrese " + ubicacion.toUpperCase() + "[1-10].");
                    continue;
                }
                
                if (!esAsientoDisponible(nombreAsiento)) {
                    System.out.println("No disponible: " + nombreAsiento); // DEBUG - ASIENTO 2: Verificar disponibilidad del asiento (compra directa)
                    System.out.println("Asiento ya ocupado, elija otro.");
                    continue;
                }
                System.out.println("Asiento válido y disponible: " + nombreAsiento); // DEBUG - ASIENTO 3: Asiento válido y disponible para compra directa
                break;
            }

            // Pedir tipo de cliente
            while (true) {
                System.out.print("Tipo de cliente (Normal/Estudiante/Tercera Edad): ");
                tipoCliente = scanner.nextLine().toLowerCase();
                if (esTipoClienteValido(tipoCliente)) {
                    break;
                }
                System.out.println("Tipo no válido, ingrese Normal, Estudiante o Tercera Edad.");
            }

            // Pedir cantidad
            while (true) {
                System.out.print("Cantidad de entradas (máximo 10): ");
                try {
                    cantidad = scanner.nextInt();
                    scanner.nextLine();
                    if (cantidad <= 0 || cantidad > ASIENTOS_POR_UBICACION || cantidad > entradasDisponibles) {
                        System.out.println("Cantidad no válida, ingrese entre 1 y "
                                + Math.min(ASIENTOS_POR_UBICACION, entradasDisponibles) + ".");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Ingrese un número válido.");
                    scanner.nextLine();
                }
            }

            marcarAsiento(nombreAsiento, 'X');
        }

        // Calcular precio
        if (ubicacion.equals("vip")) {
            precioBase = PRECIO_VIP;
        } else if (ubicacion.equals("platea")) {
            precioBase = PRECIO_PLATEA;
        } else {
            precioBase = PRECIO_GENERAL;
        }

        if (tipoCliente.equals("estudiante")) {
            descuento = precioBase * DESCUENTO_ESTUDIANTE;
        } else if (tipoCliente.equals("tercera edad")) {
            descuento = precioBase * DESCUENTO_TERCERA_EDAD;
        }

        double precioFinal = (precioBase - descuento) * cantidad;

        // Generar código de boleta
        if (ubicacion.equals("vip")) {
            contadorBoletasVip++;
            codigoBoleta = "T-VIP-" + String.format("%03d", contadorBoletasVip);
        } else if (ubicacion.equals("platea")) {
            contadorBoletasPlatea++;
            codigoBoleta = "T-PLA-" + String.format("%03d", contadorBoletasPlatea);
        } else {
            contadorBoletasGeneral++;
            codigoBoleta = "T-GEN-" + String.format("%03d", contadorBoletasGeneral);
        }

        // Guardar venta
        codigosVentas[totalVentas] = codigoBoleta;
        ubicacionesVentas[totalVentas] = ubicacion;
        asientosVentas[totalVentas] = nombreAsiento;
        tiposClienteVentas[totalVentas] = tipoCliente;
        preciosVentas[totalVentas] = precioFinal;
        cantidadesVentas[totalVentas] = cantidad;
        totalVentas++;

        entradasDisponibles -= cantidad;
        contadorVentas++;

        System.out.printf("Compra realizada: %d entradas, %s, Asiento %s, Código: %s, Precio: $%.0f CLP\n",
                cantidad, ubicacion, nombreAsiento, codigoBoleta, precioFinal);
    }

    // Modificar venta o reserva
    private void modificarVentaOReserva(Scanner scanner) {
        System.out.println("¿Modificar venta o reserva? (venta/reserva): ");
        String tipo = scanner.nextLine().toLowerCase();
        boolean esVenta = tipo.equals("venta");

        if (esVenta) {
            // Modificar venta
            int indiceVenta = -1;
            while (true) {
                System.out.print("Código de boleta (ej. T-VIP-001) o 'salir': ");
                String codigo = scanner.nextLine().toUpperCase();
                if (codigo.equals("SALIR")) {
                    System.out.println("Modificación cancelada.");
                    return;
                }
                for (int i = 0; i < totalVentas; i++) {
                    if (codigosVentas[i] != null && codigosVentas[i].equals(codigo)) {
                        indiceVenta = i;
                        break;
                    }
                }
                if (indiceVenta != -1) {
                    break;
                }
                System.out.println("Venta no encontrada.");
            }

            // Liberar recursos
            entradasDisponibles += cantidadesVentas[indiceVenta];
            marcarAsiento(asientosVentas[indiceVenta], 'O');
            if (ubicacionesVentas[indiceVenta].equals("vip")) {
                contadorBoletasVip--;
            } else if (ubicacionesVentas[indiceVenta].equals("platea")) {
                contadorBoletasPlatea--;
            } else {
                contadorBoletasGeneral--;
            }

            // Eliminar venta
            for (int i = indiceVenta; i < totalVentas - 1; i++) {
                codigosVentas[i] = codigosVentas[i + 1];
                ubicacionesVentas[i] = ubicacionesVentas[i + 1];
                asientosVentas[i] = asientosVentas[i + 1];
                tiposClienteVentas[i] = tiposClienteVentas[i + 1];
                preciosVentas[i] = preciosVentas[i + 1];
                cantidadesVentas[i] = cantidadesVentas[i + 1];
            }
            totalVentas--;

            System.out.println("Venta eliminada. Ingrese nuevos datos para comprar.");
            comprarEntradas(scanner);
        } else {
            // Modificar reserva
            int indiceReserva = -1;
            while (true) {
                System.out.print("Código de reserva (ej. R-VIP-001) o 'salir': ");
                String codigo = scanner.nextLine().toUpperCase();
                if (codigo.equals("SALIR")) {
                    System.out.println("Modificación cancelada.");
                    return;
                }
                for (int i = 0; i < totalReservas; i++) {
                    if (codigosReservas[i] != null && codigosReservas[i].equals(codigo) && !esReservaExpirada(i)) {
                        indiceReserva = i;
                        break;
                    }
                }
                if (indiceReserva != -1) {
                    break;
                }
                System.out.println("Reserva no encontrada o expirada.");
            }

            // Mostrar detalles
            System.out.println("\nReserva actual:");
            System.out.println("Código: " + codigosReservas[indiceReserva]);
            System.out.println("Ubicación: " + ubicacionesReservas[indiceReserva]);
            System.out.println("Asiento: " + asientosReservas[indiceReserva]);
            System.out.println("Tipo de cliente: " + tiposClienteReservas[indiceReserva]);
            System.out.println("Cantidad: " + cantidadesReservas[indiceReserva]);

            // Liberar asiento
            marcarAsiento(asientosReservas[indiceReserva], 'O');

            // Pedir nuevos datos
            String nuevaUbicacion;
            while (true) {
                System.out.print("Nueva ubicación (VIP/Platea/General): ");
                nuevaUbicacion = scanner.nextLine().toLowerCase();
                if (esUbicacionValida(nuevaUbicacion)) {
                    break;
                }
                System.out.println("Ubicación no válida, ingrese VIP, Platea o General.");
            }

            String nuevoAsiento;
            while (true) {
                mostrarMapaAsientos(nuevaUbicacion);
                System.out.print("Nuevo asiento (ej. " + nuevaUbicacion.toUpperCase() + "1): ");
                nuevoAsiento = scanner.nextLine().toUpperCase();
                if (!validarAsiento(nuevoAsiento, nuevaUbicacion)) {
                    System.out.println("Asiento no válido, ingrese " + nuevaUbicacion.toUpperCase() + "[1-10].");
                    continue;
                }
                if (!esAsientoDisponible(nuevoAsiento)) {
                    System.out.println("Asiento ya ocupado, elija otro.");
                    continue;
                }
                break;
            }

            String nuevoTipoCliente;
            while (true) {
                System.out.print("Nuevo tipo de cliente (Normal/Estudiante/Tercera Edad): ");
                nuevoTipoCliente = scanner.nextLine().toLowerCase();
                if (esTipoClienteValido(nuevoTipoCliente)) {
                    break;
                }
                System.out.println("Tipo no válido, ingrese Normal, Estudiante o Tercera Edad.");
            }

            int nuevaCantidad;
            while (true) {
                System.out.print("Nueva cantidad (máximo 10): ");
                try {
                    nuevaCantidad = scanner.nextInt();
                    scanner.nextLine();
                    if (nuevaCantidad <= 0 || nuevaCantidad > ASIENTOS_POR_UBICACION || nuevaCantidad > entradasDisponibles) {
                        System.out.println("Cantidad no válida, ingrese entre 1 y "
                                + Math.min(ASIENTOS_POR_UBICACION, entradasDisponibles) + ".");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Ingrese un número válido.");
                    scanner.nextLine();
                }
            }

            // Actualizar reserva
            ubicacionesReservas[indiceReserva] = nuevaUbicacion;
            asientosReservas[indiceReserva] = nuevoAsiento;
            tiposClienteReservas[indiceReserva] = nuevoTipoCliente;
            cantidadesReservas[indiceReserva] = nuevaCantidad;
            tiemposReservas[indiceReserva] = System.currentTimeMillis();
            marcarAsiento(nuevoAsiento, 'X');

            System.out.println("Reserva modificada. Código: " + codigosReservas[indiceReserva]);
        }
    }

    // Imprimir boleta
    private void imprimirBoleta(Scanner scanner) {
        int indiceVenta = -1;
        while (true) {
            System.out.print("Código de boleta (ej. T-VIP-001) o 'salir': ");
            String codigo = scanner.nextLine().toUpperCase();
            if (codigo.equals("SALIR")) {
                System.out.println("Impresión cancelada.");
                return;
            }
            for (int i = 0; i < totalVentas; i++) {
                System.out.println("Buscando código: " + codigo + ", comparando con: " + codigosVentas[i]); // DEBUG - BOLETA 1: Verificar código de boleta buscado
                if (codigosVentas[i] != null && codigosVentas[i].equals(codigo)) {
                    indiceVenta = i;
                    break;
                }
            }
            if (indiceVenta != -1) {
                break;
            }
            System.out.println("Venta no encontrada.");
        }

        System.out.println("\n=== BOLETA TEATRO MORO ===");
        
        System.out.println("Boleta encontrada, código: " + codigosVentas[indiceVenta]); // DEBUG - BOLETA 2: Código de boleta encontrado
        System.out.println("Código: " + codigosVentas[indiceVenta]);
        System.out.println("Número de venta: " + (indiceVenta + 1));
        System.out.println("Ubicación: " + ubicacionesVentas[indiceVenta]);
        System.out.println("Asiento: " + asientosVentas[indiceVenta]);
        System.out.println("Tipo de cliente: " + tiposClienteVentas[indiceVenta]);
        System.out.println("Cantidad: " + cantidadesVentas[indiceVenta]);
        System.out.println("Precio total: " + preciosVentas[indiceVenta]); // DEBUG - BOLETA 3: Precio total de la boleta
        System.out.printf("Precio total: $%.0f CLP\n", preciosVentas[indiceVenta]);
        System.out.println("=============================");

        mostrarMapaAsientos(ubicacionesVentas[indiceVenta]);
        System.out.println("Asiento: " + asientosVentas[indiceVenta]);
    }
}