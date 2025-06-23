package com.blackmarkpet.restapi.repository;

import com.blackmarkpet.restapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Metodo para iniciar sesión 
    Optional<Usuario> findByEmailAndPassword(String email, String password);

    // Metodo para verificar si un email ya está registrado
    Optional<Usuario> findByEmail(String email);
}