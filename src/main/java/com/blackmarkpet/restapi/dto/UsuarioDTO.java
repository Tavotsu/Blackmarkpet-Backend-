package com.blackmarkpet.restapi.dto;

import jakarta.validation.constraints.Email;

public class UsuarioDTO {
    
    @Email(message = "El formato del email no es válido")
    private String email;
    
    private String password;

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
