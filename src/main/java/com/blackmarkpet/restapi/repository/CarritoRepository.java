package com.blackmarkpet.restapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blackmarkpet.restapi.model.Carrito;
import com.blackmarkpet.restapi.model.Usuario;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    
    // Método para encontrar un carrito por el ID del usuario
    Optional<Carrito> findByUsuarioId(Long usuarioId);

    Optional<Carrito> findByUsuario(Usuario usuario);
    
}
