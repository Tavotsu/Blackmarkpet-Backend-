package com.blackmarkpet.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blackmarkpet.restapi.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{
    
}
