package com.museo.catalogoarte.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class PasswordManager {

    public static String hashPassword(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Primero se añade la sal
            md.update(salt);
            // Luego se añaden los bytes de la contraseña y se calcula el hash de la combinación
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No se pudo encontrar el algoritmo de hash", e);
        }
    }

    public static boolean checkPassword(String plainPassword, String storedHash) {
        try {
            byte[] combined = Base64.getDecoder().decode(storedHash);

            byte[] salt = Arrays.copyOfRange(combined, 0, 16);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Se usa la misma sal para calcular el hash de la contraseña ingresada
            md.update(salt);
            byte[] hashedPassword = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));

            byte[] storedPasswordHash = Arrays.copyOfRange(combined, 16, combined.length);

            // Se comparan los dos hashes
            return Arrays.equals(hashedPassword, storedPasswordHash);
        } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
            System.err.println("Error al verificar la contraseña: " + e.getMessage());
            return false;
        }
    }
}