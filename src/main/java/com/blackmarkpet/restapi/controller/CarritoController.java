package com.blackmarkpet.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.blackmarkpet.restapi.dto.ItemCarritoDTO;
import com.blackmarkpet.restapi.model.Carrito;
import com.blackmarkpet.restapi.model.ItemCarrito;
import com.blackmarkpet.restapi.model.Producto;
import com.blackmarkpet.restapi.model.Usuario;
import com.blackmarkpet.restapi.repository.CarritoRepository;
import com.blackmarkpet.restapi.repository.ProductoRepository;
import com.blackmarkpet.restapi.repository.UsuarioRepository;

import java.util.stream.Collectors;
import java.util.*;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*")
public class CarritoController {

    @Autowired private CarritoRepository carritoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProductoRepository productoRepository;

    // YA NO NECESITAMOS LA CLASE DTO AQUÍ. ¡MÁS LIMPIO!

    // OBTENER EL CARRITO DE UN USUARIO
    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<ItemCarritoDTO>> getCarrito(@PathVariable Long usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId).orElse(null);
        if (carrito == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        // La lógica sigue siendo la misma, usando el DTO importado
        List<ItemCarritoDTO> carritoDTO = carrito.getItems().stream().map(ItemCarritoDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(carritoDTO);
    }

    // AÑADIR UN PRODUCTO AL CARRITO
    @PostMapping("/agregar")
    public ResponseEntity<List<ItemCarritoDTO>> agregarAlCarrito(@RequestBody Map<String, Long> request) {
        Long usuarioId = request.get("usuarioId");
        Long productoId = request.get("productoId");
        
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Producto producto = productoRepository.findById(productoId).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId).orElseGet(() -> {
            Carrito nuevoCarrito = new Carrito();
            nuevoCarrito.setUsuario(usuario);
            return nuevoCarrito;
        });

        Optional<ItemCarrito> itemExistente = carrito.getItems().stream()
            .filter(item -> item.getProducto().getId().equals(productoId))
            .findFirst();

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + 1);
        } else {
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(1);
            carrito.getItems().add(nuevoItem);
        }

        carritoRepository.save(carrito);

        return getCarrito(usuarioId);
    }

    // ELIMINAR UN ITEM DEL CARRITO
    @DeleteMapping("/eliminar/{itemId}")
    public ResponseEntity<Void> eliminarDelCarrito(@PathVariable Long itemId, @RequestParam Long usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId).orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        
        carrito.getItems().removeIf(item -> item.getId().equals(itemId));
        
        carritoRepository.save(carrito);
        
        return ResponseEntity.ok().build();
    }
}