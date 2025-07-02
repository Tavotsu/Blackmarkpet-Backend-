package com.blackmarkpet.restapi.controller;

import com.blackmarkpet.restapi.dto.AgregarItemRequestDTO; 
import com.blackmarkpet.restapi.dto.ItemCarritoDTO;
import com.blackmarkpet.restapi.dto.ProductoDTO;
import com.blackmarkpet.restapi.model.Carrito;
import com.blackmarkpet.restapi.model.ItemCarrito;
import com.blackmarkpet.restapi.model.Producto;
import com.blackmarkpet.restapi.model.Usuario;
import com.blackmarkpet.restapi.repository.CarritoRepository;
import com.blackmarkpet.restapi.repository.ItemCarritoRepository;
import com.blackmarkpet.restapi.repository.ProductoRepository;
import com.blackmarkpet.restapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "https://black-markpet-frontend.vercel.app/")
public class CarritoController {

    @Autowired
    private CarritoRepository carritoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<ItemCarritoDTO>> getCarrito(@PathVariable Long usuarioId) {
        Optional<Carrito> carritoOpt = carritoRepository.findByUsuarioId(usuarioId);
        if (carritoOpt.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        Carrito carrito = carritoOpt.get();
        List<ItemCarrito> items = itemCarritoRepository.findByCarrito(carrito);
        List<ItemCarritoDTO> itemDTOs = items.stream().map(item -> {
            Producto producto = item.getProducto();
            ProductoDTO productoDTO = new ProductoDTO();
            productoDTO.setId(producto.getId());
            productoDTO.setNombre(producto.getNombre());
            productoDTO.setPrecio(producto.getPrecio());
            productoDTO.setImageUrl(producto.getImageUrl());
            ItemCarritoDTO itemDto = new ItemCarritoDTO();
            itemDto.setId(item.getId());
            itemDto.setCantidad(item.getCantidad());
            itemDto.setProducto(productoDTO);
            return itemDto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(itemDTOs);
    }

    //Agrega un producto al carrito de un usuario.
    @PostMapping("/agregar")
    public ResponseEntity<ItemCarrito> agregarAlCarrito(@RequestBody AgregarItemRequestDTO request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(request.getUsuarioId());
        Optional<Producto> productoOpt = productoRepository.findById(request.getProductoId());

        if (usuarioOpt.isEmpty() || productoOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = usuarioOpt.get();
        Producto producto = productoOpt.get();
        Carrito carrito = carritoRepository.findByUsuario(usuario).orElseGet(() -> {
            Carrito nuevoCarrito = new Carrito();
            nuevoCarrito.setUsuario(usuario);
            return carritoRepository.save(nuevoCarrito);
        });

        Optional<ItemCarrito> itemExistente = itemCarritoRepository.findByCarritoAndProducto(carrito, producto);
        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + request.getCantidad());
            return ResponseEntity.ok(itemCarritoRepository.save(item));
        } else {
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(request.getCantidad());
            return ResponseEntity.ok(itemCarritoRepository.save(nuevoItem));
        }
    }

    @DeleteMapping("/eliminar/{itemId}")
    public ResponseEntity<Void> eliminarDelCarrito(@PathVariable Long itemId) {
        if (itemCarritoRepository.existsById(itemId)) {
            itemCarritoRepository.deleteById(itemId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}