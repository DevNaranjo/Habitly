package com.habitly.data;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Utilidad técnica para el cifrado y descifrado de flujos de objetos.
 * Implementa AES-256 en modo GCM con IV aleatorio y derivación de clave PBKDF2
 * para garantizar la máxima seguridad e integridad de la persistencia local.
 * 
 * @author DevNaranjo
 * @version 1.0.7-F
 * @since 1.0.3
 */
public class CryptoManager {

    private static final String ALGORITMO = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128; // bits
    private static final int IV_LENGTH = 12; // bytes (recomendado para GCM)
    private static final int SALT_LENGTH = 16; // bytes
    private static final int KEY_LENGTH = 256; // bits
    private static final int ITERATIONS = 65536; // OWASP recommendation
    
    /**
     * Obtiene o genera una clave maestra de un archivo local 'habitly.key'
     */
    private static char[] obtenerLlaveMaestraInterna() throws Exception {
        String keyPath = com.habitly.config.AppConfig.getStoragePath() + File.separator + "habitly.key";
        File keyFile = new File(keyPath);
        byte[] keyBytes;
        if (!keyFile.exists()) {
            com.habitly.config.AppConfig.asegurarDirectorio();
            SecureRandom random = new SecureRandom();
            keyBytes = new byte[32];
            random.nextBytes(keyBytes);
            Files.write(Paths.get(keyPath), keyBytes);
        } else {
            keyBytes = Files.readAllBytes(Paths.get(keyPath));
        }
        return bytesToHex(keyBytes).toCharArray();
    }

    /**
     * Deriva una clave AES-256 segura usando PBKDF2 a partir de la contraseña y una sal aleatoria.
     */
    private static SecretKey generarLlave(byte[] salt) throws Exception {
        char[] masterKey = obtenerLlaveMaestraInterna();
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(masterKey, salt, ITERATIONS, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    /**
     * Serializa un objeto a formato binario, lo cifra con AES-GCM y lo almacena en disco.
     * El archivo resultante tiene el formato: [SALT (16 bytes)] + [IV (12 bytes)] + [CIPHERTEXT]
     */
    public static void guardarObjetoCifrado(Serializable objeto, String nombreArchivo) {
        try {
            // 1. Serializar el objeto a bytes en claro
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(objeto);
            byte[] bytesClaros = baos.toByteArray();

            // 2. Generar Salt e IV criptográficamente seguros
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            byte[] iv = new byte[IV_LENGTH];
            random.nextBytes(iv);

            // 3. Derivar la clave e inicializar el Cipher
            SecretKey secretKey = generarLlave(salt);
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            // 4. Cifrar
            byte[] bytesCifrados = cipher.doFinal(bytesClaros);

            // 5. Construir el paquete final: Salt + IV + Cifrado
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(salt);
            outputStream.write(iv);
            outputStream.write(bytesCifrados);
            
            byte[] paqueteFinal = outputStream.toByteArray();
            Files.write(Paths.get(nombreArchivo), paqueteFinal);

        } catch (IOException e) {
            System.err.println("Error de E/S al guardar: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error crítico en cifrado GCM: " + e.getMessage());
        }
    }

    /**
     * Recupera un archivo del disco, extrae el SALT y el IV, lo descifra con AES-GCM y lo reconstruye.
     */
    public static Object leerObjetoCifrado(String nombreArchivo) {
        try {
            if (!Files.exists(Paths.get(nombreArchivo))) return null;

            byte[] paqueteCompleto = Files.readAllBytes(Paths.get(nombreArchivo));
            
            // Validar longitud mínima
            if (paqueteCompleto.length < SALT_LENGTH + IV_LENGTH) {
                System.err.println("Archivo corrupto o formato no soportado (V1.1 requerido).");
                return null;
            }

            // 1. Extraer Salt, IV y Ciphertext
            byte[] salt = Arrays.copyOfRange(paqueteCompleto, 0, SALT_LENGTH);
            byte[] iv = Arrays.copyOfRange(paqueteCompleto, SALT_LENGTH, SALT_LENGTH + IV_LENGTH);
            byte[] bytesCifrados = Arrays.copyOfRange(paqueteCompleto, SALT_LENGTH + IV_LENGTH, paqueteCompleto.length);

            // 2. Derivar la clave e inicializar el Cipher
            SecretKey secretKey = generarLlave(salt);
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            // 3. Descifrar (valida automáticamente la integridad GCM Tag)
            byte[] bytesClaros = cipher.doFinal(bytesCifrados);

            // 4. Reconstruir el Objeto Java
            ByteArrayInputStream bais = new ByteArrayInputStream(bytesClaros);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();

        } catch (javax.crypto.AEADBadTagException e) {
            System.err.println("[!] ALERTA DE SEGURIDAD: Los datos han sido manipulados o la clave es incorrecta.");
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al reconstruir objeto: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error crítico en descifrado GCM: " + e.getMessage());
            return null;
        }
    }

    /**
     * Genera un salt aleatorio de 16 bytes.
     */
    public static byte[] generarSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Devuelve el hash PBKDF2 de una contraseña usando el salt provisto.
     */
    public static String hashPassword(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 256);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return bytesToHex(hash);
    }

    /**
     * Convierte bytes a una cadena hexadecimal.
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Convierte una cadena hexadecimal a bytes.
     */
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                 + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }
}