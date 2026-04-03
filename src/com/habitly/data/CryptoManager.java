package com.habitly.data;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CryptoManager {

    private static final String ALGORITMO = "AES";

    private static SecretKeySpec generarLlave() throws Exception {
        String semilla = System.getProperty("user.name") +
                System.getProperty("os.arch") +
                "Habitly_Salt_2024";

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] llaveHaseada = sha.digest(semilla.getBytes(StandardCharsets.UTF_8));

        // AES-128 necesita solo los primeros 16 bytes (128 bits)
        byte[] llave16Bytes = Arrays.copyOf(llaveHaseada, 16);

        return new SecretKeySpec(llave16Bytes, ALGORITMO);
    }

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