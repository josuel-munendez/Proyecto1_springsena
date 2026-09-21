package com.usuarios.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio de encriptación de contraseñas usando BCrypt.
 *
 * BCrypt es un algoritmo de hashing adaptativo: incrementa el coste
 * computacional con el tiempo, haciendo que los ataques de fuerza bruta
 * sean cada vez más costosos. El salt se genera automáticamente en
 * cada hash, por lo que dos contraseñas iguales producen hashes
 * diferentes (previene rainbow tables).
 *
 * Flujo:
 *  - guardar: passwordPlano → encoder.encode() → hashBCrypt → MySQL
 *  - login:   passwordPlano + hashAlmacenado → encoder.matches() → true/false
 */
@Service
public class PasswordService {

    private final BCryptPasswordEncoder encoder;

    public PasswordService() {
        this.encoder = new BCryptPasswordEncoder();
    }

    /**
     * Hashea una contraseña en texto plano con BCrypt.
     *
     * @param plainPassword contraseña sin encriptar.
     * @return hash BCrypt (60 caracteres).
     */
    public String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    /**
     * Verifica si una contraseña en texto plano coincide con un hash BCrypt.
     *
     * @param plainPassword contraseña sin encriptar.
     * @param hashedPassword hash almacenado en la BD.
     * @return true si coinciden.
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}
