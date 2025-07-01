package com.blackmarkpet.restapi.repository;

import com.blackmarkpet.restapi.model.Carrito;
import com.blackmarkpet.restapi.model.ItemCarrito;
import com.blackmarkpet.restapi.model.Producto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

    List<ItemCarrito> findByCarrito(Carrito carrito);

    Optional<ItemCarrito> findByCarritoAndProducto(Carrito carrito, Producto producto);
}
