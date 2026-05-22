package com.habitly.data;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Utilidad técnica para el cifrado y descifrado de flujos de objetos.
 * Implementa el estándar AES-128 para garantizar la privacidad de los datos locales.
 * * @author DevNaranjo
 * @version 1.0.6
 * @since 1.0.3
 */

public class CryptoManager {

    private static final String ALGORITMO = "AES";

    /**
     * Genera una clave secreta única basada en el hardware y el usuario del sistema.
     */
    private static SecretKeySpec generarLlave() throws Exception {
        String semilla = System.getProperty("user.name") +
                System.getProperty("os.arch") +
                "Habitly_Salt_2024";

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] llaveHaseada = sha.digest(semilla.getBytes(StandardCharsets.UTF_8));

        byte[] llave16Bytes = Arrays.copyOf(llaveHaseada, 16);
        return new SecretKeySpec(llave16Bytes, ALGORITMO);
    }

    /**
     * Serializa un objeto a formato binario, lo cifra y lo almacena en un archivo físico.
     */
    public static void guardarObjetoCifrado(Serializable objeto, String nombreArchivo) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(objeto);
            byte[] bytesClaros = baos.toByteArray();

            byte[] bytesCifrados = cifrar(bytesClaros);
            Files.write(Paths.get(nombreArchivo), bytesCifrados);

        } catch (IOException e) {
            System.err.println("Error de E/S al guardar: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error crítico en cifrado: " + e.getMessage());
        }
    }

    /**
     * Recupera un archivo del disco, lo descifra y lo reconstruye como un Objeto Java.
     */
    public static Object leerObjetoCifrado(String nombreArchivo) {
        try {
            if (!Files.exists(Paths.get(nombreArchivo))) return null;

            byte[] bytesCifrados = Files.readAllBytes(Paths.get(nombreArchivo));
            byte[] bytesClaros = descifrar(bytesCifrados);

            ByteArrayInputStream bais = new ByteArrayInputStream(bytesClaros);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al reconstruir objeto: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error crítico en descifrado: " + e.getMessage());
            return null;
        }
    }

    // --- MÉTODOS DE BYTES ---

    public static byte[] cifrar(byte[] datos) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, generarLlave());
        return cipher.doFinal(datos);
    }

    public static byte[] descifrar(byte[] datosCifrados) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.DECRYPT_MODE, generarLlave());
        return cipher.doFinal(datosCifrados);
    }
}