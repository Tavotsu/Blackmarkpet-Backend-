package com.blackmarkpet.restapi.controller;
import com.blackmarkpet.restapi.model.Producto;
import com.blackmarkpet.restapi.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "https://black-markpet-frontend.vercel.app/")
public class ProductoController {

    
    @Autowired
    private ProductoRepository productoRepository;


    @GetMapping
    public List<Producto> listarTodosLosProductos() {
        return productoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
    
        Optional<Producto> productoOptional = productoRepository.findById(id);
        // Si el producto existe, lo devolvemos con un HTTP 200 OK
        return productoOptional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto detallesProducto) {
        Optional<Producto> productoOptional = productoRepository.findById(id);

        if (productoOptional.isPresent()) {
            Producto productoExistente = productoOptional.get();
            productoExistente.setNombre(detallesProducto.getNombre());
            productoExistente.setDescripcion(detallesProducto.getDescripcion());
            productoExistente.setPrecio(detallesProducto.getPrecio());
            productoExistente.setStock(detallesProducto.getStock());
            productoExistente.setImageUrl(detallesProducto.getImageUrl());

            return ResponseEntity.ok(productoRepository.save(productoExistente));
        } else {
            // Si no se encuentra el producto, devolvemos un error 404
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        // Verificamos si el producto existe antes de intentar borrarlo.
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            // HTTP 204 No Content: indica que la operación fue exitosa pero no hay nada que devolver.
            return ResponseEntity.noContent().build();
        } else {
            // Si no existe, devolvemos 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
}