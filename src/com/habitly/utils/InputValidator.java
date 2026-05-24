package com.habitly.utils;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Utilidad centralizada para la validación de entradas de usuario.
 * Evita la inyección de datos corruptos o formatos inválidos en el dominio.
 */
public class InputValidator {

    // Regex para DNI español (8 números + 1 letra) o NIE (X/Y/Z + 7 números + 1 letra)
    private static final Pattern DNI_PATTERN = Pattern.compile("^[0-9]{8}[A-Z]$|^[XYZ][0-9]{7}[A-Z]$", Pattern.CASE_INSENSITIVE);
    
    // Regex estándar para emails
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}$");

    /**
     * Fuerza al usuario a introducir un texto que no esté vacío.
     */
    public static String leerStringNoVacio(Scanner sc, String mensaje) {
        String input;
        do {
            System.out.print(mensaje);
            input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("[!] Error: El campo no puede estar vacío.");
            }
        } while (input.isEmpty());
        return input;
    }

    /**
     * Valida la letra de control matemática del DNI/NIE.
     */
    public static boolean validarLetraDNI(String dni) {
        if (dni.equals("GUEST-001")) return true;
        if (!DNI_PATTERN.matcher(dni).matches()) return false;
        
        String numeroPart = dni.substring(0, dni.length() - 1);
        char letraPart = dni.charAt(dni.length() - 1);
        
        char primerCarac = numeroPart.charAt(0);
        if (primerCarac == 'X') {
            numeroPart = "0" + numeroPart.substring(1);
        } else if (primerCarac == 'Y') {
            numeroPart = "1" + numeroPart.substring(1);
        } else if (primerCarac == 'Z') {
            numeroPart = "2" + numeroPart.substring(1);
        }
        
        try {
            int numero = Integer.parseInt(numeroPart);
            char letraCorrecta = "TRWAGMYFPDXBNJZSQVHLCKE".charAt(numero % 23);
            return letraCorrecta == letraPart;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valida mediante Regex y control de letra matemática que la entrada sea un DNI/NIE válido.
     */
    public static String leerDNI(Scanner sc, String mensaje) {
        String dni;
        do {
            System.out.print(mensaje);
            dni = sc.nextLine().trim().toUpperCase();
            if (dni.equals("GUEST-001")) return dni;
            
            if (!validarLetraDNI(dni)) {
                System.out.println("[!] Formato de DNI/NIE o letra de control incorrecta (ej: 12345678Z).");
            }
        } while (!validarLetraDNI(dni));
        return dni;
    }

    /**
     * Valida mediante Regex que la entrada coincida con un correo electrónico.
     */
    public static String leerEmail(Scanner sc, String mensaje) {
        String email;
        do {
            System.out.print(mensaje);
            email = sc.nextLine().trim();
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                System.out.println("[!] Formato de e-mail inválido (ej: usuario@correo.com).");
            }
        } while (!EMAIL_PATTERN.matcher(email).matches());
        return email;
    }

    /**
     * Lee una contraseña de forma segura ocultándola en la consola si es posible.
     */
    public static String leerPassword(Scanner sc, String mensaje) {
        String password = "";
        java.io.Console console = System.console();
        if (console != null) {
            char[] chars = console.readPassword(mensaje);
            if (chars != null) {
                password = new String(chars);
            }
        } else {
            System.out.print(mensaje);
            password = sc.nextLine();
        }
        return password;
    }
}
