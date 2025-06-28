package com.blackmarkpet.restapi.controller;


import com.blackmarkpet.restapi.dto.ContactoDTO; // Importamos nuestro DTO
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "*")
public class ContactoController {

    // Cambiamos Map<String, String> por nuestro DTO.
    // La anotación @Valid le dice a Spring que aplique las validaciones que definimos en ContactoDTO.
    @PostMapping("/enviar")
    public ResponseEntity<Void> recibirMensajeContacto(@Valid @RequestBody ContactoDTO contacto) {
        
        // Ahora accedemos a los datos de forma segura a través de los getters.
        System.out.println("======================================");
        System.out.println("  NUEVO MENSAJE DE CONTACTO RECIBIDO  ");
        System.out.println("======================================");
        System.out.println("Nombre: " + contacto.getNombre());
        System.out.println("Email: " + contacto.getEmail());
        System.out.println("Mensaje: " + contacto.getMensaje());
        System.out.println("======================================");

        return ResponseEntity.ok().build();
    }
}
